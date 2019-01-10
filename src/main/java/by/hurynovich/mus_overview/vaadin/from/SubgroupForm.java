package by.hurynovich.mus_overview.vaadin.from;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class SubgroupForm extends Panel {

    private final VerticalLayout parentLayout;

    private final ComboBox<GroupDTO> groupField;

    @PropertyId("name")
    private final TextField nameField;

    private final Binder<SubgroupDTO> binder;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final GroupService groupService;

    public SubgroupForm(final GroupService groupService, final SubgroupDTO subgroupDTO,
                     final Runnable onSave, final Runnable onDiscard) {
        this.groupService = groupService;
        binder = new Binder<>(SubgroupDTO.class);
        binder.setBean(subgroupDTO);
        binder.bindInstanceFields(this);
        parentLayout = new VerticalLayout();
        groupField = new ComboBox<>("Group:");
        nameField = new TextField("Name");
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        setContent(getParentLayout(subgroupDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final SubgroupDTO subgroupDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        parentLayout.addComponents(getGroupField() , getNameField(),
                getButtonsLayout(subgroupDTO, onSave, onDiscard));
        return parentLayout;
    }

    private ComboBox getGroupField() {
        final List<GroupDTO> groups = groupService.getAllGroups();
        groupField.setItems(groups);
        return groupField;
    }

    private TextField getNameField() {
        return nameField;
    }

    private HorizontalLayout getButtonsLayout(final SubgroupDTO subgroupDTO, final Runnable onSave,
                                              final Runnable onDiscard) {
        buttonsLayout.addComponents(getSaveButton(subgroupDTO, onSave), getCancelButton(onDiscard));
        return buttonsLayout;
    }

    private Button getSaveButton(final SubgroupDTO subgroupDTO, final Runnable onSave) {
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        return cancelButton;
    }

}
