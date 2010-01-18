class OpenFileSystemController {

    def model
    def view

    void mvcGroupInit(Map args) {
        if (args.host) {
            args.host.each { model[it.key] = it.value }
        } else {
            model.fileSystem = 'file://'
        }
    }
}