package com.ruoyi.chatgpt.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.chatgpt.domain.TbRechargeableCardMain;
import com.ruoyi.chatgpt.service.ITbRechargeableCardMainService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 充值卡-主Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/cricleai/rechargeablecardmain")
public class TbRechargeableCardMainController extends BaseController
{
    @Autowired
    private ITbRechargeableCardMainService tbRechargeableCardMainService;

    /**
     * 查询充值卡-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbRechargeableCardMain tbRechargeableCardMain)
    {
        Long userId = SecurityUtils.getUserId();
        tbRechargeableCardMain.setUserId(userId);
        startPage();
        List<TbRechargeableCardMain> list = tbRechargeableCardMainService.selectTbRechargeableCardMainList(tbRechargeableCardMain);
        return getDataTable(list);
    }

    /**
     * 导出充值卡-主列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:export')")
    @Log(title = "充值卡-主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbRechargeableCardMain tbRechargeableCardMain)
    {
        List<TbRechargeableCardMain> list = tbRechargeableCardMainService.selectTbRechargeableCardMainList(tbRechargeableCardMain);
        ExcelUtil<TbRechargeableCardMain> util = new ExcelUtil<TbRechargeableCardMain>(TbRechargeableCardMain.class);
        util.exportExcel(response, list, "充值卡-主数据");
    }

    /**
     * 获取充值卡-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbRechargeableCardMainService.selectTbRechargeableCardMainById(id));
    }

    /**
     * 新增充值卡-主
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:add')")
    @Log(title = "充值卡-主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbRechargeableCardMain tbRechargeableCardMain)
    {
        return toAjax(tbRechargeableCardMainService.insertTbRechargeableCardMain(tbRechargeableCardMain));
    }

    /**
     * 修改充值卡-主
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:edit')")
    @Log(title = "充值卡-主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbRechargeableCardMain tbRechargeableCardMain)
    {
        return toAjax(tbRechargeableCardMainService.updateTbRechargeableCardMain(tbRechargeableCardMain));
    }

    /**
     * 删除充值卡-主
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:remove')")
    @Log(title = "充值卡-主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbRechargeableCardMainService.deleteTbRechargeableCardMainByIds(ids));
    }









    /**
     * 获取充值卡-主详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbRechargeableCardMainService.selectTbRechargeableCardMainByIdsys(id));
    }

    /**
     * 新增充值卡-主
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:add')")
    @Log(title = "充值卡-主", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbRechargeableCardMain tbRechargeableCardMain)
    {
        return toAjax(tbRechargeableCardMainService.insertTbRechargeableCardMainsys(tbRechargeableCardMain));
    }

    /**
     * 修改充值卡-主   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:edit')")
    @Log(title = "充值卡-主", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbRechargeableCardMain tbRechargeableCardMain)
    {
        return toAjax(tbRechargeableCardMainService.updateTbRechargeableCardMainsys(tbRechargeableCardMain));
    }


    /**
     * 下面为用到的接口
     */


    /**
     * 删除充值卡-主   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:remove')")
    @Log(title = "充值卡-主", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbRechargeableCardMainService.deleteTbRechargeableCardMainByIdssys(ids));
    }



    /**
     * 删除充值卡-主   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:remove')")
    @Log(title = "充值卡-主", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/bathId/{bathId}")
    public AjaxResult removesysBybathId(@PathVariable Long bathId)
    {
        return toAjax(tbRechargeableCardMainService.deleteTbRechargeableCardMainByBathIdsys(bathId));
    }

    /**
     * 查询充值卡-主列表   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeablecardmain:sys:list')")
    @GetMapping("/sys/list")
    public TableDataInfo listsys(TbRechargeableCardMain tbRechargeableCardMain)
    {
        startPage();
        List<TbRechargeableCardMain> list = tbRechargeableCardMainService.selectTbRechargeableCardMainListsys(tbRechargeableCardMain);
        return getDataTable(list);
    }



}
