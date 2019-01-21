package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.enumeration.UserRole;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.annotation.PrototypeScope;

@org.springframework.stereotype.Component("userRoleField")
@PrototypeScope
public class UserRoleField extends CustomField<UserRole> {

    private VerticalLayout parentLayout;

    private RadioButtonGroup<UserRole> userRoleRadioButtonGroup;

    private ListDataProvider<UserRole> dataProvider;

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final UserRole userRole) {
        userRoleRadioButtonGroup.setSelectedItem(userRole);
    }

    @Override
    public UserRole getValue() {
        return getUserRoleRadioButtonGroup().getSelectedItem().orElse(null);
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponent(getUserRoleRadioButtonGroup());
        }
        return parentLayout;
    }

    private RadioButtonGroup<UserRole> getUserRoleRadioButtonGroup() {
        if (userRoleRadioButtonGroup == null) {
            userRoleRadioButtonGroup = new RadioButtonGroup<>();
            userRoleRadioButtonGroup.setDataProvider(getDataProvider());
            userRoleRadioButtonGroup.getDataProvider().refreshAll();
        }
        return userRoleRadioButtonGroup;
    }

    private ListDataProvider<UserRole> getDataProvider() {
        if (dataProvider == null) {
            dataProvider = DataProvider.ofItems(UserRole.values());
        }
        return dataProvider;
    }

}
