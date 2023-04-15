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
import com.ruoyi.chatgpt.domain.TbAnsweEmploy;
import com.ruoyi.chatgpt.service.ITbAnsweEmployService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 回答收录列Controller
 *
 * @author ruoyi
 * @date 2023-02-10
 */
@RestController
@RequestMapping("/cricleai/employ")
public class TbAnsweEmployController extends BaseController
{
    @Autowired
    private ITbAnsweEmployService tbAnsweEmployService;

    /**
     * 查询回答收录列列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TbAnsweEmploy tbAnsweEmploy)
    {
        Long userId = SecurityUtils.getUserId();
        tbAnsweEmploy.setUserId(userId);
        startPage();
        List<TbAnsweEmploy> list = tbAnsweEmployService.selectTbAnsweEmployList(tbAnsweEmploy);
        return getDataTable(list);
    }

    /**
     * 导出回答收录列列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:export')")
    @Log(title = "回答收录列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbAnsweEmploy tbAnsweEmploy)
    {
        List<TbAnsweEmploy> list = tbAnsweEmployService.selectTbAnsweEmployList(tbAnsweEmploy);
        ExcelUtil<TbAnsweEmploy> util = new ExcelUtil<TbAnsweEmploy>(TbAnsweEmploy.class);
        util.exportExcel(response, list, "回答收录列数据");
    }

    /**
     * 获取回答收录列详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:query')")
    @GetMapping(value = "/{anserId}")
    public AjaxResult getInfo(@PathVariable("anserId") Long anserId)
    {
        return success(tbAnsweEmployService.selectTbAnsweEmployByAnserId(anserId));
    }

    /**
     * 新增回答收录列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:add')")
    @Log(title = "回答收录列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbAnsweEmploy tbAnsweEmploy)
    {
        return toAjax(tbAnsweEmployService.insertTbAnsweEmploy(tbAnsweEmploy));
    }

    /**
     * 修改回答收录列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:edit')")
    @Log(title = "回答收录列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbAnsweEmploy tbAnsweEmploy)
    {
        return toAjax(tbAnsweEmployService.updateTbAnsweEmploy(tbAnsweEmploy));
    }

    /**
     * 删除回答收录列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:remove')")
    @Log(title = "回答收录列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{anserIds}")
    public AjaxResult remove(@PathVariable Long[] anserIds)
    {
        return toAjax(tbAnsweEmployService.deleteTbAnsweEmployByAnserIds(anserIds));
    }






/**
 * 查询回答收录列列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:employ:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbAnsweEmploy tbAnsweEmploy)
    {
        startPage();
        List<TbAnsweEmploy> list = tbAnsweEmployService.selectTbAnsweEmployListsys(tbAnsweEmploy);
        return getDataTable(list);
    }



    /**
     * 获取回答收录列详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:sys:query')")
    @GetMapping(value = "/sys/{anserId}")
    public AjaxResult getInfosys(@PathVariable("anserId") Long anserId)
    {
        return success(tbAnsweEmployService.selectTbAnsweEmployByAnserIdsys(anserId));
    }

    /**
     * 新增回答收录列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:sys:add')")
    @Log(title = "回答收录列", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbAnsweEmploy tbAnsweEmploy)
    {
        return toAjax(tbAnsweEmployService.insertTbAnsweEmploysys(tbAnsweEmploy));
    }

    /**
     * 修改回答收录列   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:sys:edit')")
    @Log(title = "回答收录列", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbAnsweEmploy tbAnsweEmploy)
    {
        return toAjax(tbAnsweEmployService.updateTbAnsweEmploysys(tbAnsweEmploy));
    }

    /**
     * 删除回答收录列   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:employ:sys:remove')")
    @Log(title = "回答收录列", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{anserIds}")
    public AjaxResult removesys(@PathVariable Long[] anserIds)
    {
        return toAjax(tbAnsweEmployService.deleteTbAnsweEmployByAnserIdssys(anserIds));
    }






}
