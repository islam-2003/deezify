package be.deezify.services;

import be.deezify.exceptions.ForbiddenActionException;
import be.deezify.listeners.PlaylistServiceListener;
import be.deezify.models.Playlist;
import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.repositories.Repository;
import be.deezify.utils.AlertUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing playlists and their contents.
 */
public class PlaylistService extends ModelService<PlaylistServiceListener,PlaylistDTO, Playlist> {

    public PlaylistService(Repository<Playlist> repository) {
        super(repository);
    }

    @Override
    public Set<PlaylistDTO> getAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public Set<PlaylistDTO> getAllForUser(UserDTO user) {
        return repository.findAll().stream()
                .filter(playlist -> playlist.getOwner()
                        .map(owner -> owner.equals(user))
                        .orElse(true))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<PlaylistDTO> getById(int id) {
        return repository.findById(id).map(playlist -> playlist);
    }

    public PlaylistDTO getFavoriteForUser(UserDTO user) {
        return getAllForUser(user).stream().filter(playlistDTO -> playlistDTO.isFavorite() &&
                playlistDTO.getOwner().isPresent() && !playlistDTO.getOwner().get().isGuest()).findFirst().orElseGet(() -> {
                Playlist playlist = new Playlist("Favorite");
                playlist.setOwner(user);
                playlist.setFavorite(true);
                try {
                    save(playlist);
                    notifyPlaylistAdded(playlist);
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            return playlist;
        });
    }

    public void addTrackToFavoriteForUser(UserDTO user, TrackDTO track) {
        PlaylistDTO playlistDTO = getFavoriteForUser(user);
        getFavoriteForUser(user).addTrack(track);
        try {
            save(repository.findById(playlistDTO.getId()).get());
        } catch (Exception e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }
    }

    public void save(Playlist playlist) throws IOException {
        repository.save(playlist);
    }

    @Override
    public void delete(PlaylistDTO playlistDTO) throws IOException, ForbiddenActionException{
        if (playlistDTO.isFavorite()) {
            AlertUtils.showError("error.title.favforbidden", "error.text.favforbidden");
        }

        Optional<Playlist> playlistOptional = repository.findById(playlistDTO.getId());
        if (playlistOptional.isPresent()) {
            repository.delete(playlistOptional.get());
        }
    }

    public void addTrackToPlaylist(PlaylistDTO playlistDTO, TrackDTO trackDTO) throws IOException {
        Optional<Playlist> playlist = repository.findById(playlistDTO.getId());
        if (playlist.isPresent()) {
            playlist.get().addTrack(trackDTO);
            repository.save(playlist.get());
            notifyTrackAdded(playlistDTO, trackDTO);
        }
    }

    public void removeTrackFromPlaylist(PlaylistDTO playlistDTO, TrackDTO trackDTO) throws IOException {
        Optional<Playlist> playlist = repository.findById(playlistDTO.getId());
        if (playlist.isPresent()) {
            playlist.get().removeTrack(trackDTO);
            repository.save(playlist.get());
            notifyTrackRemoved(playlistDTO, trackDTO);
        }
    }

    public final void changeName(PlaylistDTO playlistDTO, String name) throws IOException {

        Optional<Playlist> playlist = repository.findById(playlistDTO.getId());
        if (playlist.isPresent()) {
            playlist.get().setName(name);
            repository.save(playlist.get());
            notifyPlaylistChanged(playlistDTO, PlaylistProperty.NAME);
        }
    }

    private void notifyTrackAdded(PlaylistDTO playlistDTO, TrackDTO trackDTO) {
        for (PlaylistServiceListener listener : listeners) {
            listener.onTrackAdded(playlistDTO, trackDTO);
        }
    }

    private void notifyTrackRemoved(PlaylistDTO playlistDTO, TrackDTO trackDTO) {
        for (PlaylistServiceListener listener : listeners) {
            listener.onTrackRemoved(playlistDTO, trackDTO);
        }
    }

    private void notifyPlaylistChanged(PlaylistDTO playlistDTO, PlaylistProperty trackProperty) {
        for (PlaylistServiceListener listener : listeners) {
            listener.onPlaylistChanged(playlistDTO, trackProperty);
        }
    }

    private void notifyPlaylistAdded(PlaylistDTO playlistDTO) {
        for (PlaylistServiceListener listener : listeners) {
            listener.onPlaylistAdded(playlistDTO);
        }
    }

    public enum PlaylistProperty {
        NAME
    }

}
