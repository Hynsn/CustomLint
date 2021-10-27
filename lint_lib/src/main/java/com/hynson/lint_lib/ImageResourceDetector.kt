package com.hynson.lint_lib

import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import org.jetbrains.uast.UElement
import java.io.File
import java.text.DecimalFormat
import javax.imageio.ImageIO

class ImageResourceDetector : Detector() ,Detector.ResourceFolderScanner{

    // 过滤DRAWABLE MIPMAP
    override fun appliesTo(type: ResourceFolderType): Boolean {
        return (type.compareTo(ResourceFolderType.DRAWABLE)==0) || (type.compareTo(ResourceFolderType.MIPMAP)==0)
    }

    override fun checkFolder(context: ResourceContext, folderName: String) {
        val path = context.file

        path.listFiles()?.forEach {
            if(bigImageFilter(it)){
                context.report(ISSUE1, Location.create(path),"This code mentions `lint`: **Congratulations**");
            }
        }
    }

    private fun bigImageFilter(file: File,format:DecimalFormat = DecimalFormat("0.##")) :Boolean{
        val len = file.length()
        val size = format.format(len/KB)
        if(len > KB) {
            val buf = ImageIO.read(file)
            if(buf!=null) {
                println("${file.name} ${size}kb ${buf.width}*${buf.height} ${buf.colorModel.pixelSize}bit")
                return len >= IMG_SIZE && (buf.width>=IMG_MAX_WIDTH || buf.height>= IMG_MAX_HEIGHT) && buf.colorModel.pixelSize>IMG_PIXELSIZE
            }
        }
        return false
    }

    companion object {
        var ISSUE1 = Issue.create(
            "image too large",
            "Log Usage","Please use the unified LogUtil class!",
            CORRECTNESS,6, Severity.WARNING,
            Implementation(ImageResourceDetector::class.java,Scope.RESOURCE_FOLDER_SCOPE)
        )

        var ISSUE2 = Issue.create(
            "image too large",
            "Log Usage","Please use the unified LogUtil class!",
            CORRECTNESS,6, Severity.WARNING,
            Implementation(ImageResourceDetector::class.java,Scope.RESOURCE_FOLDER_SCOPE)
        )

        var KB = 1024f

        // 过滤条件
        val IMG_SIZE = 10 * KB
        val IMG_MAX_WIDTH = 20
        val IMG_MAX_HEIGHT = 20
        val IMG_PIXELSIZE = 24
    }
}