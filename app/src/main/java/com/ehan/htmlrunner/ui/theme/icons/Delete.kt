package com.ehan.htmlrunner.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val Delete: ImageVector
  get() {
    if (_Delete != null) {
      return _Delete!!
    }
    _Delete =
      ImageVector.Builder(
          name = "Delete",
          defaultWidth = 48.dp,
          defaultHeight = 48.dp,
          viewportWidth = 48f,
          viewportHeight = 48f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(13.05f, 42f)
            quadToRelative(-1.24f, 0f, -2.12f, -0.88f)
            reflectiveQuadTo(10.05f, 39f)
            verticalLineTo(10.5f)
            horizontalLineTo(9.5f)
            quadToRelative(-0.64f, 0f, -1.07f, -0.43f)
            reflectiveQuadTo(8f, 8.99f)
            reflectiveQuadTo(8.43f, 7.92f)
            reflectiveQuadTo(9.5f, 7.5f)
            horizontalLineToRelative(7.9f)
            quadToRelative(0f, -0.65f, 0.43f, -1.08f)
            reflectiveQuadTo(18.9f, 6f)
            horizontalLineTo(29.1f)
            quadToRelative(0.64f, 0f, 1.07f, 0.43f)
            reflectiveQuadTo(30.6f, 7.5f)
            horizontalLineToRelative(7.9f)
            quadToRelative(0.64f, 0f, 1.07f, 0.43f)
            reflectiveQuadTo(40f, 9.01f)
            reflectiveQuadToRelative(-0.43f, 1.07f)
            reflectiveQuadTo(38.5f, 10.5f)
            horizontalLineTo(37.95f)
            verticalLineTo(39f)
            quadToRelative(0f, 1.24f, -0.88f, 2.12f)
            reflectiveQuadTo(34.95f, 42f)
            horizontalLineTo(13.05f)
            close()
            moveTo(34.95f, 10.5f)
            horizontalLineTo(13.05f)
            verticalLineTo(39f)
            horizontalLineToRelative(21.9f)
            verticalLineTo(10.5f)
            close()
            moveTo(20.93f, 34.27f)
            quadToRelative(0.43f, -0.43f, 0.43f, -1.07f)
            verticalLineTo(16.25f)
            quadToRelative(0f, -0.64f, -0.43f, -1.07f)
            reflectiveQuadTo(19.84f, 14.75f)
            reflectiveQuadToRelative(-1.07f, 0.43f)
            reflectiveQuadToRelative(-0.43f, 1.07f)
            verticalLineTo(33.2f)
            quadToRelative(0f, 0.64f, 0.43f, 1.07f)
            reflectiveQuadToRelative(1.07f, 0.43f)
            reflectiveQuadToRelative(1.07f, -0.43f)
            close()
            moveToRelative(8.3f, 0f)
            quadToRelative(0.43f, -0.43f, 0.43f, -1.07f)
            verticalLineTo(16.25f)
            quadToRelative(0f, -0.64f, -0.43f, -1.07f)
            reflectiveQuadTo(28.14f, 14.75f)
            reflectiveQuadToRelative(-1.07f, 0.43f)
            reflectiveQuadToRelative(-0.43f, 1.07f)
            verticalLineTo(33.2f)
            quadToRelative(0f, 0.64f, 0.43f, 1.07f)
            reflectiveQuadToRelative(1.08f, 0.43f)
            reflectiveQuadToRelative(1.07f, -0.43f)
            close()
            moveTo(13.05f, 10.5f)
            verticalLineTo(39f)
            verticalLineTo(10.5f)
            close()
          }
        }
        .build()
    return _Delete!!
  }

private var _Delete: ImageVector? = null