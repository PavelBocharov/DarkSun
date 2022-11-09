package com.mar.ds.views.itemType;

import com.mar.ds.db.entity.ItemType;
import com.mar.ds.db.entity.Product;
import com.mar.ds.db.jpa.ItemTypeRepository;
import com.mar.ds.db.jpa.ProductRepository;
import com.mar.ds.utils.DeleteDialogWidget;
import com.mar.ds.utils.ViewUtils;
import com.mar.ds.views.MainView;
import com.mar.ds.views.product.CreateProductView;
import com.mar.ds.views.product.UpdateProductView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class ItemTypeViewDialog {

    private final MainView appLayout;

    private Dialog dialog;
    private VerticalLayout products;
    private Button crtBtn;

    public ItemTypeViewDialog(MainView appLayout) {
        this.appLayout = appLayout;

        dialog = new Dialog();

        crtBtn = new Button("Создать тип предмета", new Icon(VaadinIcon.PLUS));
        crtBtn.setWidthFull();
        crtBtn.addClickListener(btnClick -> new CreateItemTypeView(this));

        reloadData();

        dialog.open();
    }


    private void initProducts() {
        products = new VerticalLayout();

        List<ItemType> itemTypeList = getRepository().findAll();

        for (ItemType itemType : itemTypeList) {
            TextField name = new TextField();
            name.setTitle("Name");
            name.setEnabled(false);
            name.setWidthFull();
            name.setValue(itemType.getName());

            Button dltBtn = new Button(new Icon(VaadinIcon.BAN), buttonClickEvent -> {
                try {
                    new DeleteDialogWidget(() -> {
                        getRepository().delete(itemType);
                        reloadData();
                    });
                } catch (Exception ex) {
                    ViewUtils.showErrorMsg("При создании произошла ошибка", ex);
                    return;
                }
            });
            dltBtn.getStyle().set("color", "red");

            Button uptBtn = new Button(
                    new Icon(VaadinIcon.PENCIL),
                    buttonClickEvent -> new UpdateItemTypeView(this, itemType)
            );
            products.add(new HorizontalLayout(name, uptBtn, dltBtn));
        }
    }

    public void reloadData() {
        try {
            initProducts();
        } catch (Exception ex) {
            ViewUtils.showErrorMsg("При создании произошла ошибка", ex);
            crtBtn.setEnabled(true);
            return;
        }
        appLayout.setContent(appLayout.getItemView().getContent());
        dialog.removeAll();
        dialog.add(
                new Label("Список типов предметов"),
                products,
                new HorizontalLayout(crtBtn, ViewUtils.getCloseButton(dialog))
        );
    }

    public ItemTypeRepository getRepository() {
        return appLayout.getRepositoryService().getItemTypeRepository();
    }
}
