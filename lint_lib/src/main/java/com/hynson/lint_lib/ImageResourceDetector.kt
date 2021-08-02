package com.hynson.lint_lib

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import java.io.File
import java.text.DecimalFormat
import javax.imageio.ImageIO

class ImageResourceDetector : Detector() ,Detector.ResourceFolderScanner{

    // 过滤DRAWABLE MIPMAP
    override fun appliesTo(type: ResourceFolderType): Boolean {
        return (type.compareTo(ResourceFolderType.DRAWABLE)==0) || (type.compareTo(ResourceFolderType.MIPMAP)==0)
    }

    override fun checkFolder(context: ResourceContext, folderName: String) {
        super.checkFolder(context, folderName)

        val path = context.file
        val format = DecimalFormat("0.##")
        path.listFiles().forEach {
            val len = it.length()
            val size = format.format(len/KB)
            if(len > KB) {
                val buf = ImageIO.read(it)
                if(buf!=null) {
                    println("${it.name} ${size}kb ${buf.width}*${buf.height} ${buf.colorModel.pixelSize}bit")
                }
            }
        }
    }

    companion object {
        var ISSUE = Issue.create(
            "image too large",
            "Log Usage","Please use the unified LogUtil class!",
            CORRECTNESS,6, Severity.ERROR,
            Implementation(ImageResourceDetector::class.java,Scope.RESOURCE_FOLDER_SCOPE)
        )

        var KB = 1024f
    }
}