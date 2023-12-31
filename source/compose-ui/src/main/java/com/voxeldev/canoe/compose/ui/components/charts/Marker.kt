package com.voxeldev.canoe.compose.ui.components.charts

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.appendCompat
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.voxeldev.canoe.dashboard.api.sumaries.DEFAULT_EMPTY_VALUE

/**
 * @author nvoxel
 */
@Composable
internal fun rememberMarker(
    projectsLabels: List<List<Pair<Float, String>>>,
    totalLabels: List<String>,
): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackground =
        remember(labelBackgroundColor) {
            ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb()).setShadow(
                radius = LABEL_BACKGROUND_SHADOW_RADIUS,
                dy = LABEL_BACKGROUND_SHADOW_DY,
                applyElevationOverlay = true,
            )
        }
    val label =
        rememberTextComponent(
            background = labelBackground,
            lineCount = projectsLabels.size + 1,
            padding = labelPadding,
            typeface = Typeface.MONOSPACE,
        )
    val indicatorInnerComponent = rememberShapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = rememberShapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = rememberShapeComponent(Shapes.pillShape, Color.White)
    val indicator =
        overlayingComponent(
            outer = indicatorOuterComponent,
            inner =
            overlayingComponent(
                outer = indicatorCenterComponent,
                inner = indicatorInnerComponent,
                innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
            ),
            innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
        )

    val guideline =
        rememberLineComponent(
            MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
            guidelineThickness,
            guidelineShape,
        )

    return remember(label, indicator, guideline, projectsLabels, totalLabels) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
                    }
                }
                labelFormatter = MarkerLabelFormatter { markedEntries, _ ->
                    markedEntries.transformToSpannableIndexed(
                        prefix = "Total: ${totalLabels[markedEntries.first().entry.x.toInt()]}\n",
                        separator = "\n",
                    ) { model, index ->
                        val currentLabel = projectsLabels[index][model.entry.x.toInt()]
                        val isShown = currentLabel.first > DEFAULT_EMPTY_VALUE
                        appendCompat(
                            if (isShown) currentLabel.second else "",
                            ForegroundColorSpan(model.color),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                        )
                        isShown
                    }
                }
            }

            override fun getInsets(
                context: MeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) = with(context) {
                outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                        LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
                        LABEL_BACKGROUND_SHADOW_DY.pixels
            }
        }
    }
}

private fun <T> List<T>.transformToSpannableIndexed(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "…",
    transform: SpannableStringBuilder.(T, Int) -> Boolean,
): Spannable {
    val buffer = SpannableStringBuilder()
    buffer.append(prefix)
    var count = 0
    var applySeparator = true
    for (index in this.indices) {
        if (++count > 1 && applySeparator) buffer.append(separator)
        if (limit < 0 || count <= limit) applySeparator = buffer.transform(this[index], index) else break
    }
    if (limit in 0..<count) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val GUIDELINE_DASH_LENGTH_DP = 8f
private const val GUIDELINE_GAP_LENGTH_DP = 4f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val labelHorizontalPaddingValue = 16.dp
private val labelVerticalPaddingValue = 16.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
private val guidelineThickness = 2.dp
private val guidelineShape = DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)
