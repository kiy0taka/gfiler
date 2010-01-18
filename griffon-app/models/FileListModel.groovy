import groovy.beans.Bindable
import org.apache.commons.vfs.*

class FileListModel {
    @Bindable String path
    FileSystemOptions opts
    @Bindable List files
}