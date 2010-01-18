import static javax.swing.JSplitPane.HORIZONTAL_SPLIT
import java.awt.dnd.*

application(title:'gfiler',
  size:[800, 600],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]
) {
    menuBar {
        menu(text:'File') {
            menuItem(openAction)
        }
    }
    splitPane(id:'mainPanel', resizeWeight: 0.50F) {
        ltab = tabbedPane(id:'ltab')
        ltab.addTab("home", buildMVCGroup('FileList', "FileListView", path:"file:///${System.getProperty('user.home')}").view.fileListPane)
        rtab = tabbedPane(id:'rtab')
    }
}