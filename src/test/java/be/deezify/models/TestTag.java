package be.deezify.models;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestTag {

    @Test
    void testConstructorSetsFields() {
        Tag tag = new Tag("Chill", "Relaxing music", Color.LIGHTBLUE);

        assertEquals(0, tag.getId());
        assertEquals("Chill", tag.getName());
        assertEquals("Relaxing music", tag.getDescription());
        assertEquals(Color.LIGHTBLUE, tag.getColor());
        assertFalse(tag.isInternal());
    }

    @Test
    void testPredefinedTagsAreInternal() {
        assertTrue(Tag.OST.isInternal());
        assertTrue(Tag.SUMMER.isInternal());
        assertTrue(Tag.BLUE.isInternal());
        assertTrue(Tag.BEIGE.isInternal());
        assertTrue(Tag.FIREBRICK.isInternal());
        assertTrue(Tag.CYAN.isInternal());
        assertTrue(Tag.ORANGE.isInternal());
    }

    @Test
    void testInternalTagsSetContainsAllPredefinedTags() {
        Set<Tag> internalTags = Tag.getInternalTags();

        assertEquals(7, internalTags.size());
        assertTrue(internalTags.contains(Tag.OST));
        assertTrue(internalTags.contains(Tag.SUMMER));
        assertTrue(internalTags.contains(Tag.BLUE));
        assertTrue(internalTags.contains(Tag.BEIGE));
        assertTrue(internalTags.contains(Tag.FIREBRICK));
        assertTrue(internalTags.contains(Tag.CYAN));
        assertTrue(internalTags.contains(Tag.ORANGE));
    }

    @Test
    void testWithersCreateModifiedCopies() {
        Tag original = new Tag("Focus", "Study music", Color.BEIGE);
        Tag modified = original.withName("Work").withColor(Color.GRAY);

        assertEquals("Focus", original.getName());
        assertEquals("Work", modified.getName());
        assertEquals(Color.BEIGE, original.getColor());
        assertEquals(Color.GRAY, modified.getColor());
    }

    @Test
    void testToStringContainsImportantFields() {
        Tag tag = new Tag("Energetic", "Workout vibes", Color.RED);
        String str = tag.toString();

        assertTrue(str.contains("Energetic"));
        assertTrue(str.contains("Workout vibes"));
    }

    @Test
    void testBuilderCreatesTagCorrectly() {
        Tag tag = Tag.builder()
                .id(5)
                .name("Party")
                .description("Party music")
                .color(Color.MAGENTA)
                .internal(false)
                .build();

        assertEquals(5, tag.getId());
        assertEquals("Party", tag.getName());
        assertEquals("Party music", tag.getDescription());
        assertEquals(Color.MAGENTA, tag.getColor());
        assertFalse(tag.isInternal());
    }
}
