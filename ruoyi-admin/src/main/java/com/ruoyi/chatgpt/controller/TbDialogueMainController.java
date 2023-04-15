package com.ruoyi.chatgpt.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.chatgpt.domain.TbModelTable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.chatgpt.service.ITbDialogueMainService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 对话列-主Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/cricleai/diglogue")
public class TbDialogueMainController extends BaseController
{
    @Autowired
    private ITbDialogueMainService tbDialogueMainService;
    /**
     * 查询对话列-主列表
     */
//    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbDialogueMain tbDialogueMain)
    {
        Long userId = SecurityUtils.getUserId();
        tbDialogueMain.setUserId(userId);
        startPage();
        List<TbDialogueMain> list = tbDialogueMainService.selectTbDialogueMainList(tbDialogueMain);
        return getDataTable(list);
    }

    /**
     * 导出对话列-主列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:export')")
    @Log(title = "对话列-主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbDialogueMain tbDialogueMain)
    {
        List<TbDialogueMain> list = tbDialogueMainService.selectTbDialogueMainList(tbDialogueMain);
        ExcelUtil<TbDialogueMain> util = new ExcelUtil<TbDialogueMain>(TbDialogueMain.class);
        util.exportExcel(response, list, "对话列-主数据");
    }

    /**
     * 获取对话列-主详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbDialogueMainService.selectTbDialogueMainById(id));
    }

    /**
     * 新增对话列-主
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:add')")
    @Log(title = "对话列-主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbDialogueMain tbDialogueMain)
    {
        return toAjax(tbDialogueMainService.insertTbDialogueMain(tbDialogueMain));
    }

    /**
     * 修改对话列-主
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:edit')")
    @Log(title = "对话列-主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbDialogueMain tbDialogueMain)
    {
        return toAjax(tbDialogueMainService.updateTbDialogueMain(tbDialogueMain));
    }

    /**
     * 删除对话列-主
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:remove')")
    @Log(title = "对话列-主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbDialogueMainService.deleteTbDialogueMainByIds(ids));
    }






/**
 * 查询对话列-主列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:diglogue:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbDialogueMain tbDialogueMain)
    {
        startPage();
        List<TbDialogueMain> list = tbDialogueMainService.selectTbDialogueMainListsys(tbDialogueMain);
        return getDataTable(list);
    }



    /**
     * 获取对话列-主详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbDialogueMainService.selectTbDialogueMainByIdsys(id));
    }

    /**
     * 新增对话列-主
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:sys:add')")
    @Log(title = "对话列-主", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbDialogueMain tbDialogueMain)
    {
        return toAjax(tbDialogueMainService.insertTbDialogueMainsys(tbDialogueMain));
    }

    /**
     * 修改对话列-主   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:sys:edit')")
    @Log(title = "对话列-主", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbDialogueMain tbDialogueMain)
    {
        return toAjax(tbDialogueMainService.updateTbDialogueMainsys(tbDialogueMain));
    }

    /**
     * 删除对话列-主   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogue:sys:remove')")
    @Log(title = "对话列-主", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbDialogueMainService.deleteTbDialogueMainByIdssys(ids));
    }



    /**
     * 新增对话列-主
     */
    @Log(title = "新建对话类型")
    @GetMapping("/creatDigFlag")
    public TableDataInfo creatDig(TbDialogueMain tbDialogueMain)
    {
        startPage();
        List<TbDialogueMain> list = tbDialogueMainService.creatDigFlag(tbDialogueMain);
        return getDataTable(list);
    }

    /**
     * 新增对话列-主
     */
    @Log(title = "新建对话")
    @PostMapping("/creatNewDig")
    public AjaxResult creatNewDig(@RequestBody TbModelTable tbModelTable)
    {
        TbDialogueMain tbDialogueMain = tbDialogueMainService.creatNewDig(tbModelTable);
        return success(tbDialogueMain);
    }


    /**
     * 新增对话列-主
     */
    @Log(title = "删除对话")
    @PostMapping("/deteleDig")
    public AjaxResult deteleDig(@RequestParam Long id)
    {
        int tbDialogueMain = tbDialogueMainService.deteleDig(id);
        return success(tbDialogueMain);
    }

}
