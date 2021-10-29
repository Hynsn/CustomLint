package com.hynson.lint_lib

import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.APP_SIZE
import java.io.File
import java.text.DecimalFormat
import javax.imageio.ImageIO

class ImageResourceDetector : Detector() ,Detector.BinaryResourceScanner{

    // 过滤DRAWABLE MIPMAP
    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return (folderType.compareTo(ResourceFolderType.DRAWABLE)==0) || (folderType.compareTo(ResourceFolderType.MIPMAP)==0)
    }

    override fun checkBinaryResource(context: ResourceContext) {
        handleFilter(context,context.file)
    }

    /**
     * 方法2
     * 1. 实现ResourceFolderScanner接口
     * 2. Scope类型修改为RESOURCE_FOLDER_SCOPE
     */
    override fun checkFolder(context: ResourceContext, folderName: String) {
        val path = context.file
        path.listFiles()?.forEach {
            handleFilter(context,it)
        }
    }

    private fun handleFilter(context: ResourceContext,file: File, format:DecimalFormat = DecimalFormat("0.##")){
        val len = file.length()
        //val size = format.format(len/KB)
        if(len > KB) {
            val buf = ImageIO.read(file)
            if(buf!=null) {
                if(len >= imgPair.first.size && (buf.width>=imgPair.first.width || buf.height>= imgPair.first.height) && buf.colorModel.pixelSize>imgPair.first.pixel){
                    context.report(ISSUE1, Location.create(file),"This code mentions `lint`: **Congratulations**");
                }
                else if(len <= imgPair.second.size && (buf.width<=imgPair.second.width || buf.height>= imgPair.second.height) && buf.colorModel.pixelSize>imgPair.second.pixel){
                    context.report(ISSUE2, Location.create(file),"This code mentions `lint`: **Congratulations**");
                }
                else{
                    //println("${file.name} ${size}kb ${buf.width}*${buf.height} ${buf.colorModel.pixelSize}bit")
                }
            }
        }
    }

    companion object {
        var ISSUE1 = Issue.create(
            "Image too Large",
            "图片太大了","图片太大了，建议使用webp格式替换",
            APP_SIZE,6, Severity.WARNING,
            Implementation(ImageResourceDetector::class.java,Scope.BINARY_RESOURCE_FILE_SCOPE)
        )

        var ISSUE2 = Issue.create(
            "Image Need Up",
            "图片需要优化","小尺寸图片建议降低图片位深度，比如8bit、16bit",
            APP_SIZE,6, Severity.WARNING,
            Implementation(ImageResourceDetector::class.java,Scope.BINARY_RESOURCE_FILE_SCOPE)
        )

        var KB = 1024f

        // 过滤条件
        val filter1 = ImageFilter(10 * KB,20,20,24)
        val filter2 = ImageFilter(10 * KB,20,20,32)
        val imgPair = Pair(filter1,filter2)
    }
    data class ImageFilter(
        val size:Float,
        val width:Int,
        val height:Int,
        val pixel:Int,
    )
}