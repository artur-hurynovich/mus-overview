package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.vaadin.custom_field.SubgroupGroupField;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component("subgroupForm")
public class SubgroupForm extends AbstractDTOForm<SubgroupDTO> {

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    @Autowired
    @Qualifier("subgroupService")
    private ISubgroupDTOService subgroupService;

    @Autowired
    @Qualifier("subgroupGroupField")
    private SubgroupGroupField groupField;

    private TextField nameField;

    private SubgroupDTO subgroupDTO;

    private Runnable onSave;

    private Runnable onDiscard;

    @PostConstruct
    public void init() {
        groupField.setCaption("Groups:");
        setupButtonsLayout();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    @Override
    public void setupForm(final SubgroupDTO subgroupDTO, final Runnable onSave, final Runnable onDiscard) {
        this.subgroupDTO = subgroupDTO;
        getBinder().readBean(subgroupDTO);
        this.onSave = onSave;
        this.onDiscard = onDiscard;
    }

    private void setupButtonsLayout() {
        setupSaveButton();
        setupCancelButton();
        getButtonsLayout().addComponents(getSaveButton(), getCancelButton());
    }

    private void setupSaveButton() {
        getSaveButton().addClickListener(clickEvent -> {
            if (getBinder().writeBeanIfValid(subgroupDTO)) {
                if (subgroupDTO.getId() == 0) {
                    subgroupService.save(subgroupDTO);
                    Notification.show("Subgroup \'" + subgroupDTO.getName() + "\' created!",
                            Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    subgroupService.update(subgroupDTO);
                    Notification.show("Subgroup updated!",
                            Notification.Type.HUMANIZED_MESSAGE);
                }
                onSave.run();
            } else {
                final String validationError = getBinder().validate().getValidationErrors().stream().
                        map(ValidationResult::getErrorMessage).collect(Collectors.joining(";\n"));
                Notification.show("Warning!\n" + validationError,
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupCancelButton() {
        getCancelButton().addClickListener(clickEvent -> onDiscard.run());
    }

    private void setupBinder() {
        getBinder().forField(getGroupField()).withValidator(groupId -> groupId != null && groupId != 0,
                "Please, select the corresponding group!").
                bind(SubgroupDTO::getGroupId, SubgroupDTO::setGroupId);
        getBinder().forField(getNameField()).withValidator(name -> name != null && !name.isEmpty(),
                "Please, enter the group name!").bind(SubgroupDTO::getName, SubgroupDTO::setName);
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getNameField(), getGroupField(), getButtonsLayout());
    }

    private SubgroupGroupField getGroupField() {
        return groupField;
    }

    private TextField getNameField() {
        if (nameField == null) {
            nameField = new TextField("Name:");
        }
        return nameField;
    }

}
