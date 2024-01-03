package com.voxeldev.canoe.compose.ui.components.decompose

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.isEnter
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation

/**
 * @author nvoxel
 */
@OptIn(FaultyDecomposeApi::class)
@Composable
internal fun <T : Any> slideTabAnimation(
    orientation: Orientation = Orientation.Horizontal,
    index: T.() -> Int,
): StackAnimation<Any, T> =
    stackAnimation { child, otherChild, direction ->
        val childIndex = child.instance.index()
        val otherChildIndex = otherChild.instance.index()
        val anim = slide(orientation = orientation)
        if ((childIndex > otherChildIndex) == direction.isEnter) anim else anim.flipSide()
    }

private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, isInitial, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            isInitial = isInitial,
            onFinished = onFinished,
            content = content,
        )
    }

private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }
