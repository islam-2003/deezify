package be.deezify.services;

import be.deezify.exceptions.ForbiddenActionException;
import be.deezify.listeners.TrackServiceListener;
import be.deezify.models.Track;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.repositories.Repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing tracks and their metadata.
 * Supports notifying listeners about changes.
 */
public class TrackService extends ModelService<TrackServiceListener, TrackDTO, Track> {

    public TrackService(Repository<Track> repository) {
        super(repository);
    }

    @Override
    public Set<TrackDTO> getAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Set<TrackDTO> getAllForUser(UserDTO user) {
        return repository.findAll().stream()
                .filter(track -> track.getOwner()
                        .map(owner -> owner.equals(user))
                        .orElse(true))
                .collect(Collectors.toSet());
    }

    public TrackDTO getRandomForUser(UserDTO user) throws ForbiddenActionException {
        List<TrackDTO> tracks = new ArrayList<>(getAllForUser(user));

        if (tracks.isEmpty()) {
            throw new ForbiddenActionException("Impossible de lancer une musique aléatoire si aucune Track n'existe");
        }

        Random rand = new Random();
        return tracks.get(rand.nextInt(tracks.size()));
    }

    public Set<TrackDTO> getFilteredAllTracksForUser(UserDTO user, String searchKey) {
        return getAllForUser(user).stream().filter(track -> track.getArtist().toLowerCase().contains(searchKey.toLowerCase())
                || track.getName().toLowerCase().contains(searchKey.toLowerCase())
                || track.getTags().stream().anyMatch(tag -> tag.getName().toLowerCase().contains(searchKey.toLowerCase()))
                || track.getAlbum().map(s -> s.toLowerCase().contains(searchKey.toLowerCase())).orElse(false))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a track by its ID.
     *
     * @param id The track ID.
     * @return Optional of the found track.
     */
    @Override
    public Optional<TrackDTO> getById(int id) {
        return repository.findById(id).map(track -> track);
    }

    public void save(Track track) throws IOException {
        repository.save(track);
        notifyTrackAdded(track);
    }


    @Override
    public void delete(TrackDTO track) throws IOException {
        Optional<Track> trackOptional = repository.findById(track.getId());
        if (trackOptional.isPresent()) {
            repository.delete(trackOptional.get());
            notifyTrackRemoved(track);
        }
    }

    public void removeTagFromTrack(TrackDTO trackDTO, TagDTO tagDTO) throws IOException {
        Optional<Track> trackOptional = repository.findById(trackDTO.getId());
        if (trackOptional.isPresent()) {
            trackOptional.get().removeTag(tagDTO);
            repository.save(trackOptional.get());
            notifyTrackChanged(trackDTO, TrackProperty.TAG);
        }
    }

    public void addTagToTrack(TrackDTO trackDTO, TagDTO tagDTO) throws IOException {
        Optional<Track> trackOptional = repository.findById(trackDTO.getId());
        if (trackOptional.isPresent()) {
            trackOptional.get().addTag(tagDTO);
            repository.save(trackOptional.get());
            notifyTrackChanged(trackDTO, TrackProperty.TAG);
        }
    }

    public final void changeName(TrackDTO trackDTO, String name) throws IllegalArgumentException, IOException {
        if (!isNameValid(name)) {
            throw new IllegalArgumentException();
        }

        Optional<Track> trackOptional = repository.findById(trackDTO.getId());
        if (trackOptional.isPresent()) {
            trackOptional.get().setName(name);
            repository.save(trackOptional.get());
            notifyTrackChanged(trackDTO, TrackProperty.NAME);
        }
    }

    public final void changeArtist(TrackDTO trackDTO, String name) throws IllegalArgumentException, IOException{
        Optional<Track> trackOptional = repository.findById(trackDTO.getId());
        if (trackOptional.isPresent()) {
            trackOptional.get().setArtist(name);
            repository.save(trackOptional.get());
            notifyTrackChanged(trackDTO, TrackProperty.ARTIST);
        }
    }

    public final void changeAlbum(TrackDTO trackDTO, String name) throws IllegalArgumentException, IOException {
        Optional<Track> trackOptional = repository.findById(trackDTO.getId());
        if (trackOptional.isPresent()) {
            trackOptional.get().setAlbum(name);
            repository.save(trackOptional.get());
            notifyTrackChanged(trackDTO, TrackProperty.ALBUM);
        }
    }

    public void removeTagFromAllTrack(TagDTO tagDTO) throws IOException {
        for (TrackDTO track : getAll()) {
            removeTagFromTrack(track, tagDTO);
        }
    }

    public void reloadTracks() {
        // TODO Implement the logic here
        // See Repo
    }

    /**
     * Notifies listeners that a track was added.
     */
    public void notifyTrackAdded(TrackDTO trackDTO) {
        for (TrackServiceListener listener : listeners) {
            listener.onTrackAdded(trackDTO);
        }
    }

    /**
     * Notifies listeners that a track was removed.
     */
    public void notifyTrackRemoved(TrackDTO trackDTO) {
        for (TrackServiceListener listener : listeners) {
            listener.onTrackRemoved(trackDTO);
        }
    }

    /**
     * Notifies listeners that a track’s property changed.
     */
    public void notifyTrackChanged(TrackDTO trackDTO, TrackProperty trackProperty) {
        for (TrackServiceListener listener : listeners) {
            listener.onTrackChanged(trackDTO, trackProperty);
        }
    }

    /**
     * Enum representing modifiable track properties.
     */
    public enum TrackProperty {
        TAG,
        ARTIST,
        ALBUM,
        NAME
    }

    private boolean isNameValid(String name) {
        return repository.findAll().stream().noneMatch(track -> track.getName().equals(name));
    }
}
