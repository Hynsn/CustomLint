# Android自定义Lint
1. LogDetector  不允许直接使用Log
2. PngResourceDetector 扫描Png 大图检查 扫描的res文件
图片过滤条件 文件大小 像素大于 > 多大，像素多大应该使用矢量图替换或者使用2bit 4bit 8bit低位图替换
3. ButterKnifeDetector 扫描项目中Activity/Fragment使用butterknife的地方
