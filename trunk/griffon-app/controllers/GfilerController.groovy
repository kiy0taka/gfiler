import static javax.swing.JOptionPane.*

class GfilerController {

    def model
    def view

    void mvcGroupInit(Map args) {
    }

    def open = {
        def ofs = buildMVCGroup('Preference')
        def dialog = ofs.view.dialog
        int x = app.appFrames[0].x + (app.appFrames[0].width - dialog.width) / 2
        int y = app.appFrames[0].y + (app.appFrames[0].height - dialog.height) / 2
        dialog.setLocation(x, y)
        dialog.show()
        dialog.hide()
    }
}