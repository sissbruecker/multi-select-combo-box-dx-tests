package com.example.application.views.updates;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.HashSet;
import java.util.stream.Collectors;

public class SoftwareUpdateFormSolution extends FormLayout {

    private SoftwareUpdate editedSoftwareUpdate = new SoftwareUpdate();
    private final BeanValidationBinder<SoftwareUpdate> binder;

    public SoftwareUpdateFormSolution(TeslaModelService teslaModelService) {
        TextField version = new TextField("Version");
        DatePicker releaseDate = new DatePicker("Release Date");
        add(version, releaseDate);

        binder = new BeanValidationBinder<>(SoftwareUpdate.class);
        binder.forField(version).bind(SoftwareUpdate::getVersion, SoftwareUpdate::setVersion);
        binder.forField(releaseDate).bind(SoftwareUpdate::getReleaseDate, SoftwareUpdate::setReleaseDate);

        /* Task 1:
         * Make SoftwareUpdate.models editable using a multi-select combo box.
         * The user must select at least one model.
         * You can use the TeslaModelService to get a list of all selectable models.
         */
        MultiSelectComboBox<TeslaModel> models = new MultiSelectComboBox<>("Models");
        models.setItemLabelGenerator(TeslaModel::getName);
        models.setItems(teslaModelService.listAll());
        add(models);

        binder.forField(models).asRequired().bind(SoftwareUpdate::getModels, SoftwareUpdate::setModels);

        /* Task 2:
         * Display the currently selected models in a text box (for example a read-only text field) at the end of the form.
         */
        TextArea selectedModels = new TextArea("Selected models");
        selectedModels.setReadOnly(true);
        add(selectedModels);
        models.addValueChangeListener(e -> {
           String modelsText = e.getValue().stream().map(TeslaModel::getName).collect(Collectors.joining(","));
           selectedModels.setValue(modelsText);
        });

        /* Task 3:
         * Add a button that selects all models.
         * The button should only be active if no model is selected yet.
         */
        Button selectAll = new Button("Select all", e -> {
            models.setValue(new HashSet<>(teslaModelService.listAll()));
        });
        add(selectAll);
        models.addValueChangeListener(e -> {
            boolean hasEmptyValue = e.getValue().isEmpty();
            selectAll.setEnabled(hasEmptyValue);
        });
    }

    public void edit(SoftwareUpdate softwareUpdate) {
        this.editedSoftwareUpdate = softwareUpdate;
        binder.readBean(this.editedSoftwareUpdate);
    }

    public SoftwareUpdate write() throws ValidationException {
        binder.writeBean(this.editedSoftwareUpdate);
        return this.editedSoftwareUpdate;
    }

    public void clear() {
        this.edit(new SoftwareUpdate());
    }
}
