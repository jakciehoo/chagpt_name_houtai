package com.ruoyi.chatgpt.controller;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.chatgpt.domain.TbRoleTable;
import com.ruoyi.chatgpt.service.ITbRoleTableService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 角色管理Controller
 *
 * @author ruoyi
 * @date 2023-03-22
 */
@RestController
@RequestMapping("/cricleai/roleChange")
public class TbRoleTableController extends BaseController
{
    @Autowired
    private ITbRoleTableService tbRoleTableService;

    /**
     * 查询角色管理列表
     */
//    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:list')")


    /**
     * 导出角色管理列表
     */
//    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:export')")
//    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TbRoleTable tbRoleTable)
//    {
//        List<TbRoleTable> list = tbRoleTableService.selectTbRoleTableList(tbRoleTable);
//        ExcelUtil<TbRoleTable> util = new ExcelUtil<TbRoleTable>(TbRoleTable.class);
//        util.exportExcel(response, list, "角色管理数据");
//    }

    /**
     * 获取角色管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbRoleTableService.selectTbRoleTableById(id));
    }

    /**
     *
     * @param tbRoleTable
     * @return
     */
    @PostMapping("/list")
    @Anonymous
    public AjaxResult list(@RequestBody TbRoleTable tbRoleTable)
    {
//        Long userId = SecurityUtils.getUserId();
//        tbRoleTable.setUserId(userId);

        List<TbRoleTable> tbRoleTables = tbRoleTableService.selectTbRoleTableList(tbRoleTable);
        if (CollectionUtil.isNotEmpty(tbRoleTables)){
            tbRoleTables.get(0).setCheck(true);
        }
        return success(tbRoleTables);
    }


    /**
     * 新增角色管理
     */
    @Log(title = "角色管理")
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:list')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TbRoleTable tbRoleTable)
    {
        if (StrUtil.isBlank(tbRoleTable.getRoleName())){
            return  error("角色名称不可为空");
        }
        if (StrUtil.isBlank(tbRoleTable.getRoleContent())){
            return  error("角色属性不可为空");
        }
//        if (StrUtil.isBlank(tbRoleTable.getRoleImage())){
//            return  error("角色图片不可为空");
//        }
        Long userId = SecurityUtils.getUserId();
        tbRoleTable.setUserId(userId);
//        List<TbRoleTable> tbRoleTables = tbRoleTableService.selectTbRoleTableList(tbRoleTable);
//        if (CollectionUtil.isNotEmpty(tbRoleTables) && tbRoleTables.size()>10){
//            throw new RuntimeException("不可超过10个");
//        }
        return toAjax(tbRoleTableService.insertTbRoleTable(tbRoleTable));
    }

    /**
     * 修改角色管理
     */
    @Log(title = "角色管理")
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:list')")
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody TbRoleTable tbRoleTable)
    {
        if (Objects.isNull(tbRoleTable.getId())){
            throw new RuntimeException("修改失败");
        }
        tbRoleTable.setUserId(SecurityUtils.getUserId());
        TbRoleTable tbRoleTable1 = tbRoleTableService.selectTbRoleTableById(tbRoleTable.getId());
        if (Objects.isNull(tbRoleTable1)){
            throw new RuntimeException("修改失败");
        }
        return toAjax(tbRoleTableService.updateTbRoleTable(tbRoleTable));
    }

    /**
     * 删除角色管理
     */
    @Log(title = "角色管理")
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:list')")
	@PostMapping("/delete")
    public AjaxResult remove(@RequestBody TbRoleTable tbRoleTable)
    {
        if (Objects.isNull(tbRoleTable.getId())){
            throw new RuntimeException("删除失败");
        }
        tbRoleTable.setUserId(SecurityUtils.getUserId());
        TbRoleTable tbRoleTable1 = tbRoleTableService.selectTbRoleTableById(tbRoleTable.getId());
        if (Objects.isNull(tbRoleTable1)){
            throw new RuntimeException("删除失败");
        }
        return toAjax(tbRoleTableService.deleteTbRoleTableById(tbRoleTable.getId()));
    }






/**
 * 查询角色管理列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbRoleTable tbRoleTable)
    {
        startPage();
        List<TbRoleTable> list = tbRoleTableService.selectTbRoleTableListsys(tbRoleTable);
        return getDataTable(list);
    }



    /**
     * 获取角色管理详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbRoleTableService.selectTbRoleTableByIdsys(id));
    }

    /**
     * 新增角色管理
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbRoleTable tbRoleTable)
    {
        if (StrUtil.isBlank(tbRoleTable.getRoleName())){
            return  error("角色名称不可为空");
        }
        if (StrUtil.isBlank(tbRoleTable.getRoleContent())){
            return  error("角色属性不可为空");
        }
//        if (StrUtil.isBlank(tbRoleTable.getRoleImage())){
//            return  error("角色图片不可为空");
//        }
        return toAjax(tbRoleTableService.insertTbRoleTablesys(tbRoleTable));
    }

    /**
     * 修改角色管理   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbRoleTable tbRoleTable)
    {
        return toAjax(tbRoleTableService.updateTbRoleTablesys(tbRoleTable));
    }

    /**
     * 删除角色管理   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:roleChange:sys:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbRoleTableService.deleteTbRoleTableByIdssys(ids));
    }






}
