package com.voxeldev.canoe.compose.ui.theme

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * This object contains icons copy-pasted from the
 * Material Icons Extended library
 * @author nvoxel
 */
internal object AdditionalIcons {

    private var _dashboard: ImageVector? = null
    val Dashboard: ImageVector
        get() {
            if (_dashboard != null) {
                return _dashboard!!
            }
            _dashboard = materialIcon(name = "Filled.Dashboard") {
                materialPath {
                    moveTo(3.0f, 13.0f)
                    horizontalLineToRelative(8.0f)
                    lineTo(11.0f, 3.0f)
                    lineTo(3.0f, 3.0f)
                    verticalLineToRelative(10.0f)
                    close()
                    moveTo(3.0f, 21.0f)
                    horizontalLineToRelative(8.0f)
                    verticalLineToRelative(-6.0f)
                    lineTo(3.0f, 15.0f)
                    verticalLineToRelative(6.0f)
                    close()
                    moveTo(13.0f, 21.0f)
                    horizontalLineToRelative(8.0f)
                    lineTo(21.0f, 11.0f)
                    horizontalLineToRelative(-8.0f)
                    verticalLineToRelative(10.0f)
                    close()
                    moveTo(13.0f, 3.0f)
                    verticalLineToRelative(6.0f)
                    horizontalLineToRelative(8.0f)
                    lineTo(21.0f, 3.0f)
                    horizontalLineToRelative(-8.0f)
                    close()
                }
            }
            return _dashboard!!
        }

    private var _leaderboard: ImageVector? = null
    val Leaderboard: ImageVector
        get() {
            if (_leaderboard != null) {
                return _leaderboard!!
            }
            _leaderboard = materialIcon(name = "Filled.Leaderboard") {
                materialPath {
                    moveTo(7.5f, 21.0f)
                    horizontalLineTo(2.0f)
                    verticalLineTo(9.0f)
                    horizontalLineToRelative(5.5f)
                    verticalLineTo(21.0f)
                    close()
                    moveTo(14.75f, 3.0f)
                    horizontalLineToRelative(-5.5f)
                    verticalLineToRelative(18.0f)
                    horizontalLineToRelative(5.5f)
                    verticalLineTo(3.0f)
                    close()
                    moveTo(22.0f, 11.0f)
                    horizontalLineToRelative(-5.5f)
                    verticalLineToRelative(10.0f)
                    horizontalLineTo(22.0f)
                    verticalLineTo(11.0f)
                    close()
                }
            }
            return _leaderboard!!
        }

    private var _filterList: ImageVector? = null
    val FilterList: ImageVector
        get() {
            if (_filterList != null) {
                return _filterList!!
            }
            _filterList = materialIcon(name = "Filled.FilterList") {
                materialPath {
                    moveTo(10.0f, 18.0f)
                    horizontalLineToRelative(4.0f)
                    verticalLineToRelative(-2.0f)
                    horizontalLineToRelative(-4.0f)
                    verticalLineToRelative(2.0f)
                    close()
                    moveTo(3.0f, 6.0f)
                    verticalLineToRelative(2.0f)
                    horizontalLineToRelative(18.0f)
                    lineTo(21.0f, 6.0f)
                    lineTo(3.0f, 6.0f)
                    close()
                    moveTo(6.0f, 13.0f)
                    horizontalLineToRelative(12.0f)
                    verticalLineToRelative(-2.0f)
                    lineTo(6.0f, 11.0f)
                    verticalLineToRelative(2.0f)
                    close()
                }
            }
            return _filterList!!
        }

    private var _restartAlt: ImageVector? = null
    val RestartAlt: ImageVector
        get() {
            if (_restartAlt != null) {
                return _restartAlt!!
            }
            _restartAlt = materialIcon(name = "Filled.RestartAlt") {
                materialPath {
                    moveTo(12.0f, 5.0f)
                    verticalLineTo(2.0f)
                    lineTo(8.0f, 6.0f)
                    lineToRelative(4.0f, 4.0f)
                    verticalLineTo(7.0f)
                    curveToRelative(3.31f, 0.0f, 6.0f, 2.69f, 6.0f, 6.0f)
                    curveToRelative(0.0f, 2.97f, -2.17f, 5.43f, -5.0f, 5.91f)
                    verticalLineToRelative(2.02f)
                    curveToRelative(3.95f, -0.49f, 7.0f, -3.85f, 7.0f, -7.93f)
                    curveTo(20.0f, 8.58f, 16.42f, 5.0f, 12.0f, 5.0f)
                    close()
                }
                materialPath {
                    moveTo(6.0f, 13.0f)
                    curveToRelative(0.0f, -1.65f, 0.67f, -3.15f, 1.76f, -4.24f)
                    lineTo(6.34f, 7.34f)
                    curveTo(4.9f, 8.79f, 4.0f, 10.79f, 4.0f, 13.0f)
                    curveToRelative(0.0f, 4.08f, 3.05f, 7.44f, 7.0f, 7.93f)
                    verticalLineToRelative(-2.02f)
                    curveTo(8.17f, 18.43f, 6.0f, 15.97f, 6.0f, 13.0f)
                    close()
                }
            }
            return _restartAlt!!
        }
}
