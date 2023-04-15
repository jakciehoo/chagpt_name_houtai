package com.ruoyi.chatgpt.controller;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.chatgpt.domain.TbRechargeableCardProcess;
import com.ruoyi.chatgpt.service.ITbRechargeableCardProcessService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 充值卡详情Controller
 *
 * @author zhx
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/cricleai/rechargeablecardprocess")
public class TbRechargeableCardProcessController extends BaseController
{
    @Autowired
    private ITbRechargeableCardProcessService tbRechargeableCardProcessService;

    /**
     * 查询充值卡详情列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        Long userId = SecurityUtils.getUserId();
        tbRechargeableCardProcess.setUserId(userId);
        startPage();
        List<TbRechargeableCardProcess> list = tbRechargeableCardProcessService.selectTbRechargeableCardProcessList(tbRechargeableCardProcess);
        return getDataTable(list);
    }

    /**
     * 导出充值卡详情列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:export')")
    @Log(title = "充值卡详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        List<TbRechargeableCardProcess> list = tbRechargeableCardProcessService.selectTbRechargeableCardProcessList(tbRechargeableCardProcess);
        ExcelUtil<TbRechargeableCardProcess> util = new ExcelUtil<TbRechargeableCardProcess>(TbRechargeableCardProcess.class);
        util.exportExcel(response, list, "充值卡详情数据");
    }

    /**
     * 获取充值卡详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbRechargeableCardProcessService.selectTbRechargeableCardProcessById(id));
    }

    /**
     * 新增充值卡详情
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:add')")
    @Log(title = "充值卡详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        return toAjax(tbRechargeableCardProcessService.insertTbRechargeableCardProcess(tbRechargeableCardProcess));
    }

    /**
     * 修改充值卡详情
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:edit')")
    @Log(title = "充值卡详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        return toAjax(tbRechargeableCardProcessService.updateTbRechargeableCardProcess(tbRechargeableCardProcess));
    }

    /**
     * 删除充值卡详情
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:remove')")
    @Log(title = "充值卡详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbRechargeableCardProcessService.deleteTbRechargeableCardProcessByIds(ids));
    }









    /**
     * 获取充值卡详情详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbRechargeableCardProcessService.selectTbRechargeableCardProcessByIdsys(id));
    }


    /**
     * 修改充值卡详情   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:edit')")
    @Log(title = "充值卡详情", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        return toAjax(tbRechargeableCardProcessService.updateTbRechargeableCardProcesssys(tbRechargeableCardProcess));
    }



    /**
     * 下面为用到的接口
     */

    /**
     * 查询充值卡详情列表   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:list')")
    @GetMapping("/sys/list")
    public TableDataInfo listsys(TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        if (Objects.isNull(tbRechargeableCardProcess.getBatchId())){
            throw new RuntimeException("参数错误");
        }
        startPage();
        List<TbRechargeableCardProcess> list = tbRechargeableCardProcessService.selectTbRechargeableCardProcessListsys(tbRechargeableCardProcess);
        return getDataTable(list);
    }


    /**
     * 新增充值卡详情
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:add')")
    @Log(title = "充值卡详情", businessType = BusinessType.INSERT)
    @PostMapping("/sys/add")
    public AjaxResult addsys(@RequestBody TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        return toAjax(tbRechargeableCardProcessService.insertTbRechargeableCardProcesssys(tbRechargeableCardProcess));
    }



    /**
     * 删除充值卡详情   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:remove')")
    @Log(title = "充值卡详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbRechargeableCardProcessService.deleteTbRechargeableCardProcessByIdssys(ids));
    }

    /**
     * 导出充值卡详情列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:rechargeablecardprocess:sys:add')")
    @Log(title = "充值卡详情", businessType = BusinessType.EXPORT)
    @PostMapping("/sys/export")
    public void exportsys(HttpServletResponse response, TbRechargeableCardProcess tbRechargeableCardProcess)
    {
        List<TbRechargeableCardProcess> list = tbRechargeableCardProcessService.selectTbRechargeableCardProcessListsys(tbRechargeableCardProcess);
        ExcelUtil<TbRechargeableCardProcess> util = new ExcelUtil<TbRechargeableCardProcess>(TbRechargeableCardProcess.class);
        util.exportExcel(response, list, "充值卡详情数据");
    }

    @Log(title = "激活卡使用", businessType = BusinessType.EXPORT)
    @PostMapping("/use")
    public AjaxResult rechargeableCardUse(@RequestParam String cardNumber)
    {
        return toAjax(tbRechargeableCardProcessService.rechargeableCardUse(cardNumber));
    }

}
