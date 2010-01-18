import groovy.beans.Bindable

class OpenFileSystemModel {
    @Bindable String name
    @Bindable String fileSystem
    @Bindable String host
    @Bindable String username
    @Bindable String password
    @Bindable String path
    @Bindable String keyFilePath
    @Bindable String passphrase

    def asType(Class c) {
        [name: name, fileSystem: fileSystem, host: host,
            username: username, password: password, path: path,
            keyFilePath: keyFilePath,
            passphrase: passphrase]
    }
}