import static java.awt.GridBagConstraints.*

import javax.swing.*

btn = { name, enabled = bind(source: prefList, sourceEvent: 'valueChanged', sourceValue: { prefList.selectedValue != null }), c ->
    button(name, constraints: gbc(gridwidth: REMAINDER, fill: HORIZONTAL), enabled: enabled, actionPerformed: c)
}


renderer = new DefaultListCellRenderer()

dialog = dialog(size:[245, 180], modal:true) {
    panel {
        gridBagLayout()
        scrollPane(constraints: gbc(gridheight: REMAINDER)) {
            list(id:'prefList', fixedCellWidth:120, listData: bind(source: model, sourceProperty: 'preferences'),
                cellRenderer:{ list, value, index, isSelected, hasFocus ->
                    renderer.getListCellRendererComponent(list, value.name, index, isSelected, hasFocus)
                } as ListCellRenderer)
        }
        btn('Add', true) {
            controller.add()
        }
        btn('Edit') {
            controller.edit()
        }
        btn('Remove') {
            controller.remove()
        }
        btn('Open') {
            controller.open()
        }
    }
}