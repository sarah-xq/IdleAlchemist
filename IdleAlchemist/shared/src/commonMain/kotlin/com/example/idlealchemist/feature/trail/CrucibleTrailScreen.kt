package com.example.idlealchemist.feature.trail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.feature.stage.goalProgressLabel
import com.example.idlealchemist.model.AlchemistProfile
import com.example.idlealchemist.model.LevelProgress
import com.example.idlealchemist.model.StageStatus
import com.example.idlealchemist.model.TrailStage
import com.example.idlealchemist.model.goalProgress
import com.example.idlealchemist.ui.AlchemistProfileCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/** Color palette matching the HTML pixel-RPG design system. */
private val ColorAbyssalSlate = Color(0xFF0E091B)
private val ColorIronKeep = Color(0xFF1A132C)
private val ColorWornLeather = Color(0xFF271C3D)
private val ColorForgedSteel = Color(0xFF382E5C)
private val ColorAetherCyan = Color(0xFF38D6D1)
private val ColorDragonEmber = Color(0xFFE05338)
private val ColorTransmutationGold = Color(0xFFEBB434)
private val ColorWitchHazel = Color(0xFF9F5DE2)
private val ColorAgedVellum = Color(0xFFEADCC9)
private val ColorDustyAsh = Color(0xFF8E929E)

/**
 * Interactive Level Map representing the series of all levels.
 *
 * Implements the 2D scrolling forest map with connecting illuminated Bezier paths,
 * dark pine tree silhouettes, glowing spores, and interactive level nodes from the HTML spec.
 *
 * Every stage can be inspected. Unlocked and completed stages can be
 * played from the challenge dialog; locked stages explain what unlocks them.
 *
 * @param profile Player snapshot received through navigation.
 * @param stages Ordered challenge definitions with unique stage numbers.
 * @param levels Saved progress keyed by stage number.
 * @param statuses Lock state of every stage, keyed by stage number.
 * @param selectedStage Challenge currently being inspected, or null.
 * @param onStageSelected Called when a stage node or card is selected.
 * @param onDismissStage Called when the challenge dialog is closed.
 * @param onPlayStage Called when an unlocked stage is played.
 * @param onCreateAnother Called to return to profile creation.
 * @param modifier Configuration applied to the screen container.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrucibleTrailScreen(
    profile: AlchemistProfile,
    stages: List<TrailStage>,
    levels: Map<Int, LevelProgress>,
    statuses: Map<Int, StageStatus>,
    selectedStage: TrailStage?,
    onStageSelected: (TrailStage) -> Unit,
    onDismissStage: () -> Unit,
    onPlayStage: (TrailStage) -> Unit,
    onCreateAnother: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val stages = levelMap.levels
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var isListView by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Active (current) node
    val activeNode = stages.firstOrNull { statuses[it.number] == StageStatus.UNLOCKED }
        ?: stages.lastOrNull { statuses[it.number] == StageStatus.COMPLETED }
        ?: stages.firstOrNull()

    // Scroll to current active node on launch
    LaunchedEffect(levelMap.id, activeNode?.number) {
        if (!isListView && activeNode != null) {
            val yPx = activeNode.yPx
            val targetScroll = with(density) { (yPx.dp - 320.dp).toPx().toInt().coerceAtLeast(0) }
            scrollState.scrollTo(targetScroll)
        }
    }

    // Auto-dismiss toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2800)
            toastMessage = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorAbyssalSlate),
    ) {
        if (isListView) {
            // Compact List View Mode
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 90.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                val horizontalPadding = if (maxWidth >= 720.dp) 32.dp else 16.dp

                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 960.dp)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = horizontalPadding,
                        vertical = 16.dp,
                    ),
                ) {
                    item(key = "player-summary") {
                        AlchemistProfileCard(
                            profile = profile,
                            modifier = Modifier.padding(bottom = 20.dp),
                        )
                    }

                    itemsIndexed(
                        items = stages,
                        key = { _, stage -> stage.number },
                    ) { index, stage ->
                        val status = statuses[stage.number] ?: StageStatus.LOCKED
                        LevelMapNodeItem(
                            stage = stage,
                            status = status,
                            isLast = index == stages.lastIndex,
                            isSelected = selectedStage?.number == stage.number,
                            onClick = {
                                if (status == StageStatus.LOCKED) {
                                    toastMessage = "Trial ${stage.roman} (${stage.title}) is locked! Complete previous trials first."
                                } else {
                                    onStageSelected(stage)
                                }
                            },
                        )
                    }

                    item(key = "create-another") {
                        OutlinedButton(
                            onClick = onCreateAnother,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 32.dp),
                            border = BorderStroke(1.dp, ColorForgedSteel),
                        ) {
                            Text("Create another alchemist", color = ColorAgedVellum)
                        }
                    }
                }
            }
        } else {
            // Visual 2D Forest Canvas Level Map Mode (Scrollable 2700dp height)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                val canvasWidth = maxWidth
                val canvasHeight = 2700.dp
                val canvasWidthPx = with(density) { canvasWidth.toPx() }
                val canvasHeightPx = with(density) { canvasHeight.toPx() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(canvasHeight)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF07040E),
                                    Color(0xFF0E091B),
                                    Color(0xFF130C24),
                                    Color(0xFF18102E),
                                    Color(0xFF0E091B),
                                ),
                            ),
                        ),
                ) {
                    // Layer 1: Background Pine Trees & Spores
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Pine Tree Silhouettes (Left and Right edges)
                        val random = Random(42)
                        for (i in 0 until 36) {
                            val isLeft = i % 2 == 0
                            val treeY = (i / 36f) * canvasHeightPx + random.nextFloat() * 60f - 30f
                            val treeX = if (isLeft) {
                                (random.nextFloat() * 0.18f) * canvasWidthPx
                            } else {
                                (0.82f + random.nextFloat() * 0.18f) * canvasWidthPx
                            }
                            val scale = 0.75f + random.nextFloat() * 0.75f
                            val treeWidth = 120f * scale
                            val treeHeight = 190f * scale
                            val fill = if (i % 3 == 0) Color(0xFF090512) else Color(0xFF120B24)

                            val treePath = Path().apply {
                                moveTo(treeX, treeY)
                                lineTo(treeX - treeWidth / 2, treeY + treeHeight * 0.35f)
                                lineTo(treeX - treeWidth * 0.3f, treeY + treeHeight * 0.35f)
                                lineTo(treeX - treeWidth / 2 * 1.2f, treeY + treeHeight * 0.65f)
                                lineTo(treeX - treeWidth * 0.35f, treeY + treeHeight * 0.65f)
                                lineTo(treeX - treeWidth / 2 * 1.4f, treeY + treeHeight * 0.95f)
                                lineTo(treeX + treeWidth / 2 * 1.4f, treeY + treeHeight * 0.95f)
                                lineTo(treeX + treeWidth * 0.35f, treeY + treeHeight * 0.65f)
                                lineTo(treeX + treeWidth / 2 * 1.2f, treeY + treeHeight * 0.65f)
                                lineTo(treeX + treeWidth * 0.3f, treeY + treeHeight * 0.35f)
                                lineTo(treeX + treeWidth / 2, treeY + treeHeight * 0.35f)
                                close()
                            }
                            drawPath(path = treePath, color = fill, alpha = 0.5f)
                        }

                        // Floating Spores / Fireflies
                        for (i in 0 until 40) {
                            val sporeX = random.nextFloat() * canvasWidthPx
                            val sporeY = random.nextFloat() * canvasHeightPx
                            val radius = 3f + random.nextFloat() * 4f
                            val color = if (i % 2 == 0) ColorAetherCyan else ColorWitchHazel

                            drawCircle(
                                color = color.copy(alpha = 0.25f),
                                radius = radius * 2.5f,
                                center = Offset(sporeX, sporeY),
                            )
                            drawCircle(
                                color = color.copy(alpha = 0.8f),
                                radius = radius,
                                center = Offset(sporeX, sporeY),
                            )
                        }
                    }

                    // Layer 2: Connecting Winding Illuminated Bezier Path
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val pathEffectUnlocked = PathEffect.dashPathEffect(floatArrayOf(18f, 14f))
                        val pathEffectLocked = PathEffect.dashPathEffect(floatArrayOf(12f, 16f))

                        for (i in 0 until stages.size - 1) {
                            val curr = stages[i]
                            val next = stages[i + 1]

                            val x1 = (curr.xPercent / 100f) * canvasWidthPx
                            val y1 = curr.yPx * density.density
                            val x2 = (next.xPercent / 100f) * canvasWidthPx
                            val y2 = next.yPx * density.density
                            val midY = (y1 + y2) / 2f

                            val path = Path().apply {
                                moveTo(x1, y1)
                                cubicTo(x1, midY, x2, midY, x2, y2)
                            }

                            val isSegmentUnlocked = (statuses[curr.number] == StageStatus.COMPLETED) &&
                                    (statuses[next.number] == StageStatus.COMPLETED || statuses[next.number] == StageStatus.UNLOCKED)

                            if (isSegmentUnlocked) {
                                // Outer Cyan Glow
                                drawPath(
                                    path = path,
                                    color = ColorAetherCyan.copy(alpha = 0.3f),
                                    style = Stroke(width = 16f, pathEffect = pathEffectUnlocked, cap = StrokeCap.Round),
                                )
                                // Main Illuminated Path
                                drawPath(
                                    path = path,
                                    color = ColorAetherCyan,
                                    style = Stroke(width = 7f, pathEffect = pathEffectUnlocked, cap = StrokeCap.Round),
                                )
                            } else {
                                // Locked Steel Path
                                drawPath(
                                    path = path,
                                    color = ColorForgedSteel.copy(alpha = 0.8f),
                                    style = Stroke(width = 5f, pathEffect = pathEffectLocked, cap = StrokeCap.Round),
                                )
                            }
                        }
                    }

                    // Layer 3: Interactive Level Nodes & Cards
                    stages.forEach { stage ->
                        val status = statuses[stage.number] ?: StageStatus.LOCKED
                        val isCurrent = activeNode?.number == stage.number

                        val nodeX = (stage.xPercent / 100f) * canvasWidthPx
                        val nodeY = stage.yPx * density.density

                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        (nodeX - with(density) { 36.dp.toPx() }).toInt(),
                                        (nodeY - with(density) { 36.dp.toPx() }).toInt(),
                                    )
                                }
                                .size(72.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            // Floating Lantern Indicator for Active Node
                            if (isCurrent && status != StageStatus.LOCKED) {
                                Surface(
                                    color = ColorAbyssalSlate,
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(1.dp, ColorAetherCyan),
                                    shadowElevation = 6.dp,
                                    modifier = Modifier.offset(y = (-44).dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(ColorAetherCyan, CircleShape),
                                        )
                                        Text(
                                            text = "ALCHEMIST HERE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorAetherCyan,
                                            letterSpacing = 0.5.sp,
                                        )
                                    }
                                }
                            }

                            // Node Button
                            val (nodeBg, nodeBorder, contentColor) = when {
                                status == StageStatus.COMPLETED -> Triple(ColorWornLeather, ColorTransmutationGold, ColorTransmutationGold)
                                status == StageStatus.UNLOCKED && stage.isBoss -> Triple(ColorWornLeather, ColorDragonEmber, ColorDragonEmber)
                                status == StageStatus.UNLOCKED -> Triple(ColorWornLeather, ColorAetherCyan, ColorAetherCyan)
                                else -> Triple(ColorIronKeep, ColorForgedSteel, ColorDustyAsh)
                            }

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(nodeBg, RoundedCornerShape(16.dp))
                                    .border(
                                        width = if (isCurrent) 3.dp else 2.dp,
                                        color = nodeBorder,
                                        shape = RoundedCornerShape(16.dp),
                                    )
                                    .clickable {
                                        if (status == StageStatus.LOCKED) {
                                            toastMessage = "Trial ${stage.roman} (${stage.title}) is locked! Complete previous trials first."
                                        } else {
                                            onStageSelected(stage)
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = when {
                                        status == StageStatus.COMPLETED -> "✓"
                                        status == StageStatus.LOCKED -> "🔒"
                                        stage.isBoss -> "👑"
                                        else -> stage.roman
                                    },
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor,
                                )
                            }
                        }

                        // Node Title Label Card below Node
                        val cardWidthDp = 160.dp
                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        (nodeX - with(density) { (cardWidthDp / 2).toPx() }).toInt(),
                                        (nodeY + with(density) { 38.dp.toPx() }).toInt(),
                                    )
                                }
                                .width(cardWidthDp)
                                .clickable {
                                    if (status == StageStatus.LOCKED) {
                                        toastMessage = "Trial ${stage.roman} (${stage.title}) is locked! Complete previous trials first."
                                    } else {
                                        onStageSelected(stage)
                                    }
                                },
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Surface(
                                color = ColorIronKeep.copy(alpha = 0.95f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (status == StageStatus.LOCKED) ColorForgedSteel else ColorWitchHazel,
                                ),
                                shadowElevation = 6.dp,
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    if (stage.isBoss) {
                                        Text(
                                            text = "BOSS SANCTUM",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorDragonEmber,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(bottom = 2.dp),
                                        )
                                    }
                                    Text(
                                        text = "${stage.roman}. ${stage.title}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (status == StageStatus.LOCKED) ColorDustyAsh else ColorAgedVellum,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 15.sp,
                                        softWrap = true,
                                        maxLines = 2,
                                    )
                                    if (status == StageStatus.COMPLETED) {
                                        Text(
                                            text = "★★★",
                                            fontSize = 11.sp,
                                            color = ColorTransmutationGold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 2.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Layer 4: Fixed Top HUD Header
        TopFixedHUDHeader(
            worldName = levelMap.worldName,
            essenceCount = levels.values.sumOf { it.game.points.toLong() },
            isListView = isListView,
            availableMaps = availableMaps,
            currentMapId = levelMap.id,
            onSelectMap = onSelectMap,
            onToggleView = { isListView = !isListView },
            onCurrentNodeClick = {
                if (activeNode != null) {
                    val targetScroll = with(density) { (activeNode.yPx.dp - 320.dp).toPx().toInt().coerceAtLeast(0) }
                    coroutineScope.launch { scrollState.animateScrollTo(targetScroll) }
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
        )

        // Toast Notification Overlay
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
        ) {
            toastMessage?.let { msg ->
                Surface(
                    color = ColorIronKeep,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, ColorWitchHazel),
                    shadowElevation = 10.dp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Text(
                        text = msg,
                        color = ColorAgedVellum,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    )
                }
            }
        }
    }

    // Tome Level Detail Modal
    if (selectedStage != null) {
        val stageIndex = stages.indexOf(selectedStage)

        TomeLevelDetailsModal(
            stage = selectedStage,
            status = statuses[selectedStage.number] ?: StageStatus.LOCKED,
            progress = levels[selectedStage.number] ?: LevelProgress(),
            previousStage = stages.getOrNull(stageIndex - 1),
            onPlay = { onPlayStage(selectedStage) },
            onDismiss = onDismissStage,
        )
    }
}

/**
 * Secondary constructor overload for backwards compatibility.
 */
@Composable
fun CrucibleTrailScreen(
    profile: AlchemistProfile,
    stages: List<TrailStage>,
    levels: Map<Int, LevelProgress>,
    statuses: Map<Int, StageStatus>,
    selectedStage: TrailStage?,
    onStageSelected: (TrailStage) -> Unit,
    onDismissStage: () -> Unit,
    onPlayStage: (TrailStage) -> Unit,
    onCreateAnother: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultLevelMap = remember(stages) {
        LevelMap(
            id = 1,
            worldName = "The Alchemist's Tome: Pinewoods",
            levels = stages,
        )
    }

    CrucibleTrailScreen(
        profile = profile,
        levelMap = defaultLevelMap,
        availableMaps = listOf(defaultLevelMap),
        onSelectMap = {},
        levels = levels,
        statuses = statuses,
        selectedStage = selectedStage,
        onStageSelected = onStageSelected,
        onDismissStage = onDismissStage,
        onPlayStage = onPlayStage,
        onCreateAnother = onCreateAnother,
        modifier = modifier,
    )
}

/**
 * Fixed Top HUD matching the RPG Panel header in the HTML design.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopFixedHUDHeader(
    worldName: String,
    essenceCount: Long,
    isListView: Boolean,
    availableMaps: List<LevelMap>,
    currentMapId: Int,
    onSelectMap: (Int) -> Unit,
    onToggleView: () -> Unit,
    onCurrentNodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = ColorIronKeep.copy(alpha = 0.95f),
        border = BorderStroke(2.dp, ColorForgedSteel),
        shadowElevation = 8.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = worldName.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ColorTransmutationGold,
                        letterSpacing = 1.sp,
                    )
                    Text(
                        text = "${statuses.count { it.value == StageStatus.COMPLETED }} of " +
                                "${stages.size} stages complete · Finish a stage " +
                                "to unlock the next",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.secondary,
                    )
                }

                // Essence Counter
                Surface(
                    color = ColorAbyssalSlate,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, ColorForgedSteel),
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text("★", color = ColorTransmutationGold, fontSize = 12.sp)
                        Text(essenceCount.toString(), color = ColorTransmutationGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("ESSENCE", color = ColorDustyAsh, fontSize = 9.sp)
                    }
                }
            }

            itemsIndexed(
                items = stages,
                key = { _, stage -> stage.number },
            ) { index, stage ->
                StagePathItem(
                    stage = stage,
                    status = statuses[stage.number] ?: StageStatus.LOCKED,
                    isLast = index == stages.lastIndex,
                    isSelected = selectedStage?.number == stage.number,
                    onClick = {
                        onStageSelected(stage)
                    },
                )
            }

                OutlinedButton(
                    onClick = onToggleView,
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
                    border = BorderStroke(1.dp, ColorForgedSteel),
                ) {
                    Text(if (isListView) "[ MAP VIEW ]" else "[ LIST VIEW ]", fontSize = 10.sp, color = ColorAgedVellum)
                }
            }

    if (selectedStage != null) {
        val stageIndex = stages.indexOf(selectedStage)

        StageDetailsDialog(
            stage = selectedStage,
            status = statuses[selectedStage.number] ?: StageStatus.LOCKED,
            progress = levels[selectedStage.number] ?: LevelProgress(),
            previousStage = stages.getOrNull(stageIndex - 1),
            onPlay = {
                onPlayStage(selectedStage)
            },
            onDismiss = onDismissStage,
        )
    }
}

/**
 * Displays one numbered node, its connecting line, and its challenge card.
 *
 * Intrinsic row height lets the connector follow the card's text height.
 * Text may wrap without requiring a fixed card height.
 *
 * @param stage Challenge definition to display.
 * @param status Whether the stage is locked, playable, or complete.
 * @param isLast Whether the connector should end at this node.
 * @param isSelected Whether this stage is currently being inspected.
 * @param onClick Called when the node or card is selected.
 * @param modifier Configuration applied to the complete path item.
 */
@Composable
private fun LevelMapNodeItem(
    stage: TrailStage,
    status: StageStatus,
    isLast: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Column(
            modifier = Modifier
                .width(58.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val (nodeBg, nodeBorder, nodeContentColor) = when {
                status == StageStatus.COMPLETED -> Triple(ColorWornLeather, ColorTransmutationGold, ColorTransmutationGold)
                status == StageStatus.UNLOCKED && stage.isBoss -> Triple(ColorWornLeather, ColorDragonEmber, ColorDragonEmber)
                status == StageStatus.UNLOCKED -> Triple(ColorWornLeather, ColorAetherCyan, ColorAetherCyan)
                else -> Triple(ColorIronKeep, ColorForgedSteel, ColorDustyAsh)
            }

            FilledTonalButton(
                onClick = onClick,
                modifier = Modifier
                    .size(52.dp)
                    .border(2.dp, nodeBorder, CircleShape)
                    .semantics {
                        contentDescription =
                            "Inspect stage ${stage.number}: ${stage.title}, " +
                                    statusLabel(status)
                    },
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = when (status) {
                    StageStatus.COMPLETED -> ButtonDefaults.filledTonalButtonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                    )
                    StageStatus.UNLOCKED -> ButtonDefaults.filledTonalButtonColors()
                    StageStatus.LOCKED -> ButtonDefaults.filledTonalButtonColors(
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.onSurfaceVariant,
                    )
                },
            ) {
                Text(
                    text = if (status == StageStatus.COMPLETED) "✓" else stage.number.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .width(3.dp)
                        .weight(1f)
                        .background(
                            if (status == StageStatus.COMPLETED) ColorAetherCyan else ColorForgedSteel,
                        ),
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, bottom = 20.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = if (isSelected) 2.dp else 1.dp,
                color = when {
                    isSelected -> ColorAetherCyan
                    status == StageStatus.COMPLETED -> ColorTransmutationGold.copy(alpha = 0.6f)
                    stage.isBoss -> ColorDragonEmber.copy(alpha = 0.6f)
                    else -> ColorForgedSteel
                },
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) ColorWornLeather else ColorIronKeep,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "TRIAL ${stage.roman} · ${stage.element.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorWitchHazel,
                        fontWeight = FontWeight.Bold,
                    )
                    if (stage.isBoss) {
                        Surface(
                            color = ColorDragonEmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, ColorDragonEmber),
                        ) {
                            Text(
                                text = "BOSS SANCTUM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorDragonEmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                    }
                }

                Text(
                    text = stage.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (status == StageStatus.LOCKED) ColorDustyAsh else ColorAgedVellum,
                )

                Text(
                    text = stage.objective,
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorDustyAsh,
                )

                Text(
                    text = statusLabel(status),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (status == StageStatus.LOCKED) {
                        colors.onSurfaceVariant
                    } else {
                        colors.primary
                    },
                )
            }
        }
    }
}

/**
 * Short description of a stage's lock state, shown on its card.
 *
 * @param status Lock state of the stage.
 */
private fun statusLabel(status: StageStatus): String = when (status) {
    StageStatus.COMPLETED -> "Completed · tap to replay"
    StageStatus.UNLOCKED -> "Unlocked · tap to play"
    StageStatus.LOCKED -> "Locked"
}

/**
 * Displays the full objective and associated reward for a selected stage,
 * with a button to play it when it is unlocked.
 *
 * @param stage Challenge selected from the visual trail.
 * @param status Whether the stage is locked, playable, or complete.
 * @param progress Saved progress for the stage.
 * @param previousStage Stage that must be completed to unlock this one.
 * @param onPlay Called when Play or Resume is selected.
 * @param onDismiss Called when the dialog is dismissed or Close is selected.
 */
@Composable
private fun TomeLevelDetailsModal(
    stage: TrailStage,
    status: StageStatus,
    progress: LevelProgress,
    previousStage: TrailStage?,
    onPlay: () -> Unit,
    onDismiss: () -> Unit,
) {
    val started = progress.game.stats.manualClicks > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorIronKeep,
        titleContentColor = ColorTransmutationGold,
        textContentColor = ColorAgedVellum,
        shape = RoundedCornerShape(12.dp),
        title = {
            Column {
                Text(
                    text = if (stage.isBoss) "TRIAL ${stage.roman} • BOSS SANCTUM" else "TRIAL ${stage.roman}",
                    style = MaterialTheme.typography.labelMedium,
                    color = ColorWitchHazel,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stage.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = ColorTransmutationGold,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // Tome Lore Entry
                if (stage.lore.isNotEmpty()) {
                    Surface(
                        color = ColorWornLeather,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, ColorForgedSteel),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "TOME ENTRY",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorWitchHazel,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stage.lore,
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorAgedVellum,
                                lineHeight = 18.sp,
                            )
                        }
                    }
                }

                // Objective & Reward grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Recommended Brew
                    Surface(
                        color = ColorWornLeather,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, ColorForgedSteel),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "RECOMMENDED BREW",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorAetherCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stage.potion.ifEmpty { "Dewdrop Tonic" },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = ColorAgedVellum,
                            )
                        }
                    }

                    // Essence Reward
                    Surface(
                        color = ColorWornLeather,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, ColorForgedSteel),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "TRIAL REWARD",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorTransmutationGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${stage.rewardEssence} Essence",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = ColorTransmutationGold,
                            )
                        }
                    }
                }

                // Stage Objective & Progress
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "OBJECTIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorDustyAsh,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stage.objective,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ColorAgedVellum,
                    )
                }

                Text(
                    text = stage.reward,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = when {
                        status == StageStatus.LOCKED && previousStage != null ->
                            "Complete stage ${previousStage.number}: " +
                                    "${previousStage.title} to unlock this stage."
                        started ->
                            goalProgressLabel(stage.goal, progress.goalProgress(stage.goal)) +
                                    " · ${progress.game.points} essence saved"
                        else -> "Not started. Essence starts at 0 in every stage."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        confirmButton = {
            if (status != StageStatus.LOCKED) {
                TextButton(onClick = onPlay) {
                    Text(
                        when {
                            status == StageStatus.COMPLETED -> "Replay"
                            started -> "Resume"
                            else -> "Play"
                        },
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
    )
}
