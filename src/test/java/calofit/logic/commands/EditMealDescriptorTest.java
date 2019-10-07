package calofit.logic.commands;

import calofit.testutil.EditMealDescriptorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EditMealDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCommand.EditMealDescriptor descriptorWithSameValues = new EditCommand.EditMealDescriptor(CommandTestUtil.DESC_AMY);
        Assertions.assertTrue(CommandTestUtil.DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        Assertions.assertTrue(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_AMY));

        // null -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(null));

        // different types -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(5));

        // different values -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_BOB));

        // different name -> returns false
        EditCommand.EditMealDescriptor editedAmy = new EditMealDescriptorBuilder(CommandTestUtil.DESC_AMY).withName(CommandTestUtil.VALID_NAME_BOB).build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditMealDescriptorBuilder(CommandTestUtil.DESC_AMY).withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));
    }
}
