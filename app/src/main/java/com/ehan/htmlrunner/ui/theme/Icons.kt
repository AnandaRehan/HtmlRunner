package com.ehan.htmlrunner.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {

    val Add: ImageVector
        get() {
            if (_Add != null) {
                return _Add!!
            }
            _Add = ImageVector.Builder(
                name = "Add",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(11f, 13f)
                    horizontalLineTo(6f)
                    quadTo(5.58f, 13f, 5.29f, 12.71f)
                    quadTo(5f, 12.43f, 5f, 12f)
                    reflectiveQuadTo(5.29f, 11.29f)
                    reflectiveQuadTo(6f, 11f)
                    horizontalLineToRelative(5f)
                    verticalLineTo(6f)
                    quadTo(11f, 5.57f, 11.29f, 5.29f)
                    reflectiveQuadTo(12f, 5f)
                    reflectiveQuadToRelative(0.71f, 0.29f)
                    reflectiveQuadTo(13f, 6f)
                    verticalLineToRelative(5f)
                    horizontalLineToRelative(5f)
                    quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                    reflectiveQuadTo(19f, 12f)
                    reflectiveQuadToRelative(-0.29f, 0.71f)
                    reflectiveQuadTo(18f, 13f)
                    horizontalLineTo(13f)
                    verticalLineToRelative(5f)
                    quadToRelative(0f, 0.43f, -0.29f, 0.71f)
                    reflectiveQuadTo(12f, 19f)
                    reflectiveQuadTo(11.29f, 18.71f)
                    quadTo(11f, 18.43f, 11f, 18f)
                    verticalLineTo(13f)
                    close()
                }
            }.build()
            return _Add!!
        }
    private var _Add: ImageVector? = null

    val ArrowBack: ImageVector
        get() {
            if (_ArrowBack != null) {
                return _ArrowBack!!
            }
            _ArrowBack = ImageVector.Builder(
                name = "ArrowBack",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
                autoMirror = true
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(7.83f, 13f)
                    lineToRelative(4.9f, 4.9f)
                    quadToRelative(0.3f, 0.3f, 0.29f, 0.7f)
                    reflectiveQuadTo(12.7f, 19.3f)
                    quadTo(12.4f, 19.58f, 12f, 19.59f)
                    reflectiveQuadTo(11.3f, 19.3f)
                    lineTo(4.7f, 12.7f)
                    quadTo(4.55f, 12.55f, 4.49f, 12.38f)
                    reflectiveQuadTo(4.43f, 12f)
                    reflectiveQuadTo(4.49f, 11.63f)
                    reflectiveQuadTo(4.7f, 11.3f)
                    lineTo(11.3f, 4.7f)
                    quadTo(11.58f, 4.42f, 11.99f, 4.42f)
                    reflectiveQuadTo(12.7f, 4.7f)
                    quadTo(13f, 5f, 13f, 5.41f)
                    reflectiveQuadTo(12.7f, 6.13f)
                    lineTo(7.83f, 11f)
                    horizontalLineTo(19f)
                    quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                    reflectiveQuadTo(20f, 12f)
                    reflectiveQuadToRelative(-0.29f, 0.71f)
                    reflectiveQuadTo(19f, 13f)
                    horizontalLineTo(7.83f)
                    close()
                }
            }.build()
            return _ArrowBack!!
        }
    private var _ArrowBack: ImageVector? = null

    val Delete: ImageVector
        get() {
            if (_Delete != null) {
                return _Delete!!
            }
            _Delete = ImageVector.Builder(
                name = "Delete",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(7f, 21f)
                    quadToRelative(-0.825f, 0f, -1.412f, -0.587f)
                    reflectiveQuadTo(5f, 19f)
                    verticalLineTo(6f)
                    horizontalLineTo(4f)
                    verticalLineTo(4f)
                    horizontalLineToRelative(5f)
                    verticalLineTo(3f)
                    horizontalLineToRelative(6f)
                    verticalLineToRelative(1f)
                    horizontalLineToRelative(5f)
                    verticalLineToRelative(2f)
                    horizontalLineToRelative(-1f)
                    verticalLineToRelative(13f)
                    quadToRelative(0f, 0.825f, -0.587f, 1.413f)
                    reflectiveQuadTo(17f, 21f)
                    horizontalLineTo(7f)
                    close()
                    moveTo(17f, 6f)
                    horizontalLineTo(7f)
                    verticalLineToRelative(13f)
                    horizontalLineToRelative(10f)
                    verticalLineTo(6f)
                    close()
                    moveTo(9f, 17f)
                    horizontalLineToRelative(2f)
                    verticalLineTo(8f)
                    horizontalLineTo(9f)
                    verticalLineToRelative(9f)
                    close()
                    moveToRelative(4f, 0f)
                    horizontalLineToRelative(2f)
                    verticalLineTo(8f)
                    horizontalLineToRelative(-2f)
                    verticalLineToRelative(9f)
                    close()
                    moveTo(7f, 6f)
                    verticalLineToRelative(13f)
                    verticalLineTo(6f)
                    close()
                }
            }.build()
            return _Delete!!
        }
    private var _Delete: ImageVector? = null

    val Edit: ImageVector
        get() {
            if (_Edit != null) {
                return _Edit!!
            }
            _Edit = ImageVector.Builder(
                name = "Edit",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(5f, 19f)
                    horizontalLineTo(6.43f)
                    lineTo(16.2f, 9.23f)
                    lineTo(14.78f, 7.8f)
                    lineTo(5f, 17.58f)
                    verticalLineTo(19f)
                    close()
                    moveTo(4f, 21f)
                    quadTo(3.58f, 21f, 3.29f, 20.71f)
                    quadTo(3f, 20.43f, 3f, 20f)
                    verticalLineTo(17.58f)
                    quadToRelative(0f, -0.4f, 0.15f, -0.76f)
                    reflectiveQuadTo(3.58f, 16.18f)
                    lineTo(16.2f, 3.57f)
                    quadTo(16.5f, 3.3f, 16.86f, 3.15f)
                    reflectiveQuadTo(17.63f, 3f)
                    quadToRelative(0.4f, 0f, 0.78f, 0.15f)
                    reflectiveQuadTo(19.05f, 3.6f)
                    lineTo(20.43f, 5f)
                    quadToRelative(0.3f, 0.27f, 0.44f, 0.65f)
                    reflectiveQuadTo(21f, 6.4f)
                    quadToRelative(0f, 0.4f, -0.14f, 0.76f)
                    reflectiveQuadTo(20.43f, 7.82f)
                    lineTo(7.83f, 20.43f)
                    quadTo(7.55f, 20.7f, 7.19f, 20.85f)
                    quadTo(6.83f, 21f, 6.43f, 21f)
                    horizontalLineTo(4f)
                    close()
                    moveTo(19f, 6.4f)
                    lineTo(17.6f, 5f)
                    lineTo(19f, 6.4f)
                    close()
                    moveTo(15.48f, 8.52f)
                    lineTo(14.78f, 7.8f)
                    lineTo(16.2f, 9.23f)
                    lineTo(15.48f, 8.52f)
                    close()
                }
            }.build()
            return _Edit!!
        }
    private var _Edit: ImageVector? = null

    val Home: ImageVector
        get() {
            if (_Home != null) {
                return _Home!!
            }
            _Home = ImageVector.Builder(
                name = "Home",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(6f, 19f)
                    horizontalLineTo(9f)
                    verticalLineTo(14f)
                    quadTo(9f, 13.58f, 9.29f, 13.29f)
                    quadTo(9.58f, 13f, 10f, 13f)
                    horizontalLineToRelative(4f)
                    quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                    reflectiveQuadTo(15f, 14f)
                    verticalLineToRelative(5f)
                    horizontalLineToRelative(3f)
                    verticalLineTo(10f)
                    lineTo(12f, 5.5f)
                    lineTo(6f, 10f)
                    verticalLineToRelative(9f)
                    close()
                    moveTo(4f, 19f)
                    verticalLineTo(10f)
                    quadTo(4f, 9.52f, 4.21f, 9.1f)
                    quadTo(4.43f, 8.67f, 4.8f, 8.4f)
                    lineToRelative(6f, -4.5f)
                    quadTo(11.33f, 3.5f, 12f, 3.5f)
                    reflectiveQuadToRelative(1.2f, 0.4f)
                    lineToRelative(6f, 4.5f)
                    quadToRelative(0.38f, 0.28f, 0.59f, 0.7f)
                    quadTo(20f, 9.52f, 20f, 10f)
                    verticalLineToRelative(9f)
                    quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                    reflectiveQuadTo(18f, 21f)
                    horizontalLineTo(14f)
                    quadToRelative(-0.42f, 0f, -0.71f, -0.29f)
                    quadTo(13f, 20.43f, 13f, 20f)
                    verticalLineTo(15f)
                    horizontalLineTo(11f)
                    verticalLineToRelative(5f)
                    quadToRelative(0f, 0.43f, -0.29f, 0.71f)
                    reflectiveQuadTo(10f, 21f)
                    horizontalLineTo(6f)
                    quadTo(5.18f, 21f, 4.59f, 20.41f)
                    reflectiveQuadTo(4f, 19f)
                    close()
                    moveToRelative(8f, -6.75f)
                    close()
                }
            }.build()
            return _Home!!
        }
    private var _Home: ImageVector? = null

    val MoreVert: ImageVector
        get() {
            if (_MoreVert != null) {
                return _MoreVert!!
            }
            _MoreVert = ImageVector.Builder(
                name = "MoreVert",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12f, 20f)
                    quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                    reflectiveQuadTo(10f, 18f)
                    reflectiveQuadToRelative(0.59f, -1.41f)
                    reflectiveQuadTo(12f, 16f)
                    reflectiveQuadToRelative(1.41f, 0.59f)
                    quadTo(14f, 17.18f, 14f, 18f)
                    reflectiveQuadToRelative(-0.59f, 1.41f)
                    reflectiveQuadTo(12f, 20f)
                    close()
                    moveToRelative(0f, -6f)
                    quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                    reflectiveQuadTo(10f, 12f)
                    reflectiveQuadToRelative(0.59f, -1.41f)
                    reflectiveQuadTo(12f, 10f)
                    reflectiveQuadToRelative(1.41f, 0.59f)
                    quadTo(14f, 11.18f, 14f, 12f)
                    reflectiveQuadToRelative(-0.59f, 1.41f)
                    reflectiveQuadTo(12f, 14f)
                    close()
                    moveTo(12f, 8f)
                    quadTo(11.18f, 8f, 10.59f, 7.41f)
                    reflectiveQuadTo(10f, 6f)
                    reflectiveQuadTo(10.59f, 4.59f)
                    reflectiveQuadTo(12f, 4f)
                    reflectiveQuadToRelative(1.41f, 0.59f)
                    quadTo(14f, 5.18f, 14f, 6f)
                    reflectiveQuadTo(13.41f, 7.41f)
                    reflectiveQuadTo(12f, 8f)
                    close()
                }
            }.build()
            return _MoreVert!!
        }
    private var _MoreVert: ImageVector? = null

    val PlayArrow: ImageVector
        get() {
            if (_PlayArrow != null) {
                return _PlayArrow!!
            }
            _PlayArrow = ImageVector.Builder(
                name = "PlayArrow",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(8f, 17.18f)
                    verticalLineTo(6.82f)
                    quadTo(8f, 6.4f, 8.3f, 6.11f)
                    quadTo(8.6f, 5.82f, 9f, 5.82f)
                    quadToRelative(0.13f, 0f, 0.26f, 0.04f)
                    reflectiveQuadTo(9.53f, 5.97f)
                    lineToRelative(8.15f, 5.18f)
                    quadToRelative(0.23f, 0.15f, 0.34f, 0.38f)
                    quadToRelative(0.11f, 0.23f, 0.11f, 0.48f)
                    reflectiveQuadToRelative(-0.11f, 0.47f)
                    reflectiveQuadToRelative(-0.34f, 0.38f)
                    lineTo(9.53f, 18.02f)
                    quadTo(9.4f, 18.1f, 9.26f, 18.14f)
                    quadTo(9.13f, 18.18f, 9f, 18.18f)
                    quadToRelative(-0.4f, 0f, -0.7f, -0.29f)
                    reflectiveQuadTo(8f, 17.18f)
                    close()
                    moveTo(10f, 12f)
                    close()
                    moveToRelative(0f, 3.35f)
                    lineTo(15.25f, 12f)
                    lineTo(10f, 8.65f)
                    verticalLineToRelative(6.7f)
                    close()
                }
            }.build()
            return _PlayArrow!!
        }
    private var _PlayArrow: ImageVector? = null

    val Refresh: ImageVector
        get() {
            if (_Refresh != null) {
                return _Refresh!!
            }
            _Refresh = ImageVector.Builder(
                name = "Refresh",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Bevel,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12f, 20f)
                    quadTo(8.65f, 20f, 6.33f, 17.68f)
                    reflectiveQuadTo(4f, 12f)
                    reflectiveQuadTo(6.33f, 6.32f)
                    reflectiveQuadTo(12f, 4f)
                    quadToRelative(1.73f, 0f, 3.3f, 0.71f)
                    quadTo(16.88f, 5.43f, 18f, 6.75f)
                    verticalLineTo(5f)
                    quadTo(18f, 4.57f, 18.29f, 4.29f)
                    reflectiveQuadTo(19f, 4f)
                    reflectiveQuadToRelative(0.71f, 0.29f)
                    reflectiveQuadTo(20f, 5f)
                    verticalLineToRelative(5f)
                    quadToRelative(0f, 0.42f, -0.29f, 0.71f)
                    reflectiveQuadTo(19f, 11f)
                    horizontalLineTo(14f)
                    quadToRelative(-0.42f, 0f, -0.71f, -0.29f)
                    quadTo(13f, 10.43f, 13f, 10f)
                    quadTo(13f, 9.57f, 13.29f, 9.29f)
                    reflectiveQuadTo(14f, 9f)
                    horizontalLineToRelative(3.2f)
                    quadTo(16.4f, 7.6f, 15.01f, 6.8f)
                    reflectiveQuadTo(12f, 6f)
                    quadTo(9.5f, 6f, 7.75f, 7.75f)
                    reflectiveQuadTo(6f, 12f)
                    reflectiveQuadToRelative(1.75f, 4.25f)
                    reflectiveQuadTo(12f, 18f)
                    quadToRelative(1.7f, 0f, 3.11f, -0.86f)
                    quadToRelative(1.41f, -0.86f, 2.19f, -2.31f)
                    quadToRelative(0.2f, -0.35f, 0.56f, -0.49f)
                    reflectiveQuadTo(18.6f, 14.33f)
                    quadToRelative(0.4f, 0.13f, 0.57f, 0.53f)
                    reflectiveQuadTo(19.15f, 15.6f)
                    quadToRelative(-1.03f, 2f, -2.93f, 3.2f)
                    reflectiveQuadTo(12f, 20f)
                    close()
                }
            }.build()
            return _Refresh!!
        }
    private var _Refresh: ImageVector? = null
}
