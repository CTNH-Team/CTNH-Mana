package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class MultiPatternMultiblockMachine extends RecipeElectricMultiblockMachine {

    /**
     * 存储所有可用的模式
     */
    private final List<BlockPattern> patterns = new ArrayList<>();

    /**
     * 当前匹配的模式索引（-1 表示未匹配）
     */

    @Getter
    @Persisted
    public int matchedPatternIndex = -1;

    /**
     * 当前匹配的模式（缓存）
     */
    @Nullable
    public BlockPattern matchedPattern = null;

    public MultiPatternMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        // 初始化模式列表
        initializePatterns();
    }

    /**
     * 初始化模式列表
     * 子类应该重写此方法来添加模式
     */
    protected void initializePatterns() {
        // 默认从定义中获取模式（向后兼容）
        BlockPattern defaultPattern = getDefinition().getPatternFactory().get();
        if (defaultPattern != null) {
            patterns.add(defaultPattern);
        }
    }

    /**
     * 添加一个模式到列表
     *
     * @param pattern 要添加的模式
     */
    protected void addPattern(BlockPattern pattern) {
        if (pattern != null) {
            patterns.add(pattern);
        }
    }

    /**
     * 添加一个模式到列表（使用 Supplier 延迟加载）
     *
     * @param patternSupplier 模式提供者
     */
    protected void addPattern(Supplier<BlockPattern> patternSupplier) {
        if (patternSupplier != null) {
            patterns.add(patternSupplier.get());
        }
    }

    /**
     * 获取所有模式
     *
     * @return 模式列表
     */
    protected List<BlockPattern> getPatterns() {
        return patterns;
    }

    /**
     * 获取当前匹配的模式
     *
     * @return 当前匹配的模式，如果未匹配则返回 null
     */
    @Nullable
    public BlockPattern getMatchedPattern() {
        if (matchedPatternIndex >= 0 && matchedPatternIndex < patterns.size()) {
            return patterns.get(matchedPatternIndex);
        }
        return null;
    }

    /**
     * 重写 checkPattern() 方法，尝试匹配所有模式
     *
     * @return 如果任何一个模式匹配成功则返回 true
     */
    @Override
    public boolean checkPattern() {
        MultiblockState worldState = getMultiblockState();

        // 如果模式列表为空，使用默认行为
        if (patterns.isEmpty()) {
            return super.checkPattern();
        }

        // 尝试匹配每个模式
        for (int i = patterns.size() - 1; i >= 0; i--) {
            BlockPattern pattern = patterns.get(i);
            if (pattern != null) {
                // 清理状态（每次尝试前清理）
                worldState.clean();

                // 尝试匹配当前模式
                if (pattern.checkPatternAt(worldState, false)) {
                    // 匹配成功，记录索引
                    matchedPatternIndex = i;
                    matchedPattern = pattern;
                    return true;
                }
            }
        }

        // 所有模式都不匹配
        matchedPatternIndex = -1;
        matchedPattern = null;
        return false;
    }

    /**
     * 重写 getPattern() 方法，返回当前匹配的模式
     * 如果未匹配，尝试扫描所有模式找到能匹配当前世界状态的模式（用于 EMI 预览）。
     * 若全部不匹配，返回第一个模式。
     *
     * @return 当前匹配的模式或扫描结果或第一个模式
     */
    @Override
    public BlockPattern getPattern() {
        // 如果已匹配，返回匹配的模式
        if (matchedPattern != null) {
            return matchedPattern;
        }

        // 如果没有模式，返回默认行为
        if (patterns.isEmpty()) {
            return super.getPattern();
        }

        // 尝试从高级到低级扫描，找到与当前世界状态匹配的模式
        // 这在 EMI 预览时会被调用（此时 dummy world 中已放置了对应 tier 的方块）
        MultiblockState worldState = getMultiblockState();
        if (worldState != null) {
            BlockPattern matched = null;
            for (int i = patterns.size() - 1; i >= 0; i--) {
                BlockPattern p = patterns.get(i);
                if (p != null) {
                    worldState.clean();
                    if (p.checkPatternAt(worldState, false)) {
                        matched = p;
                        break;
                    }
                }
            }
            // 清理临时匹配产生的状态，让调用方重新做带谓词的检查
            worldState.clean();
            if (matched != null) {
                return matched;
            }
        }

        // 如果未匹配，返回第一个模式（用于预览）
        return patterns.get(0);
    }

    /**
     * 结构无效时重置匹配索引
     */
    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        matchedPatternIndex = -1;
        matchedPattern = null;
    }

    /**
     * 获取匹配的模式名称（用于显示）
     * 子类可以重写此方法提供自定义名称
     *
     * @return 模式名称
     */
    public String getMatchedPatternName() {
        if (matchedPatternIndex >= 0) {
            return "Pattern " + (matchedPatternIndex + 1);
        }
        return "No Pattern Matched";
    }

    /**
     * 检查是否匹配了特定索引的模式
     *
     * @param index 模式索引
     * @return 是否匹配该模式
     */
    public boolean isPatternMatched(int index) {
        return matchedPatternIndex == index;
    }
}