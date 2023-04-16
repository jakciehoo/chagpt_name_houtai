package com.ruoyi.chatgpt.controller;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.chatgpt.domain.TbModelTable;
import com.ruoyi.chatgpt.service.ITbModelTableService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 模型建设Controller
 *
 * @author zhx
 * @date 2023-04-11
 */
@RestController
@RequestMapping("/cricleai/usermodel")
public class TbModelTableController extends BaseController
{
    @Autowired
    private ITbModelTableService tbModelTableService;

    /**
     * 查询模型建设列表
     */
    @PostMapping("/list")
    @Anonymous
    public AjaxResult list(@RequestParam Long dRoleId)
    {
        if (Objects.isNull(dRoleId)){
            throw new RuntimeException("模型分类为空");
        }
//        startPage();
        TbModelTable tbModelTable =  new TbModelTable();
        tbModelTable.setDRoleId(dRoleId);
        List<TbModelTable> list = tbModelTableService.selectTbModelTableList(tbModelTable);
        return success(list);
    }


    /**
     * 导出模型建设列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:export')")
    @Log(title = "模型建设", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbModelTable tbModelTable)
    {
        List<TbModelTable> list = tbModelTableService.selectTbModelTableList(tbModelTable);
        ExcelUtil<TbModelTable> util = new ExcelUtil<TbModelTable>(TbModelTable.class);
        util.exportExcel(response, list, "模型建设数据");
    }

    /**
     * 获取模型建设详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbModelTableService.selectTbModelTableById(id));
    }

    /**
     * 新增模型建设
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:add')")
    @Log(title = "模型建设", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbModelTable tbModelTable)
    {
        return toAjax(tbModelTableService.insertTbModelTable(tbModelTable));
    }

    /**
     * 修改模型建设
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:edit')")
    @Log(title = "模型建设", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbModelTable tbModelTable)
    {
        return toAjax(tbModelTableService.updateTbModelTable(tbModelTable));
    }

    /**
     * 删除模型建设
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:remove')")
    @Log(title = "模型建设", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbModelTableService.deleteTbModelTableByIds(ids));
    }






/**
 * 查询模型建设列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbModelTable tbModelTable)
    {
        startPage();
        List<TbModelTable> list = tbModelTableService.selectTbModelTableListsys(tbModelTable);
        return getDataTable(list);
    }



    /**
     * 获取模型建设详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbModelTableService.selectTbModelTableByIdsys(id));
    }

    /**
     * 新增模型建设
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:add')")
    @Log(title = "模型建设", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbModelTable tbModelTable)
    {
        if (StrUtil.isBlank(tbModelTable.getModelName())){
            return error("模型名称为空");
        }
        if (StrUtil.isBlank(tbModelTable.getModelContent())){
            return error("模型描述为空");
        }
        if (Objects.isNull(tbModelTable.getDRoleId())){
            return error("模型分类为空");
        }
        if (Objects.isNull(tbModelTable.getIsUse())){
            tbModelTable.setIsUse(1);
        }
        return toAjax(tbModelTableService.insertTbModelTablesys(tbModelTable));
    }

    /**
     * 修改模型建设   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:edit')")
    @Log(title = "模型建设", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbModelTable tbModelTable)
    {
        return toAjax(tbModelTableService.updateTbModelTablesys(tbModelTable));
    }

    /**
     * 删除模型建设   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:remove')")
    @Log(title = "模型建设", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbModelTableService.deleteTbModelTableByIdssys(ids));
    }



    /**
     * 获取模型地址
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:usermodel:sys:remove')")
    @Log(title = "获取模型地址")
    @GetMapping("/getModelAddress")
    public AjaxResult getModelAddress()
    {
        return success(tbModelTableService.getModelAddress());
    }


}
