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
import com.ruoyi.chatgpt.domain.TbFrequencyConsumptionLog;
import com.ruoyi.chatgpt.service.ITbFrequencyConsumptionLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 次数消耗日志Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/cricleai/frequentcylog")
public class TbFrequencyConsumptionLogController extends BaseController
{
    @Autowired
    private ITbFrequencyConsumptionLogService tbFrequencyConsumptionLogService;

    /**
     * 查询次数消耗日志列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        Long userId = SecurityUtils.getUserId();
        tbFrequencyConsumptionLog.setUserId(userId);
        startPage();
        List<TbFrequencyConsumptionLog> list = tbFrequencyConsumptionLogService.selectTbFrequencyConsumptionLogList(tbFrequencyConsumptionLog);
        return getDataTable(list);
    }

    /**
     * 导出次数消耗日志列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:export')")
    @Log(title = "次数消耗日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        List<TbFrequencyConsumptionLog> list = tbFrequencyConsumptionLogService.selectTbFrequencyConsumptionLogList(tbFrequencyConsumptionLog);
        ExcelUtil<TbFrequencyConsumptionLog> util = new ExcelUtil<TbFrequencyConsumptionLog>(TbFrequencyConsumptionLog.class);
        util.exportExcel(response, list, "次数消耗日志数据");
    }

    /**
     * 获取次数消耗日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbFrequencyConsumptionLogService.selectTbFrequencyConsumptionLogById(id));
    }

    /**
     * 新增次数消耗日志
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:add')")
    @Log(title = "次数消耗日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return toAjax(tbFrequencyConsumptionLogService.insertTbFrequencyConsumptionLog(tbFrequencyConsumptionLog));
    }

    /**
     * 修改次数消耗日志
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:edit')")
    @Log(title = "次数消耗日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return toAjax(tbFrequencyConsumptionLogService.updateTbFrequencyConsumptionLog(tbFrequencyConsumptionLog));
    }

    /**
     * 删除次数消耗日志
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:remove')")
    @Log(title = "次数消耗日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbFrequencyConsumptionLogService.deleteTbFrequencyConsumptionLogByIds(ids));
    }






/**
 * 查询次数消耗日志列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        startPage();
        List<TbFrequencyConsumptionLog> list = tbFrequencyConsumptionLogService.selectTbFrequencyConsumptionLogListsys(tbFrequencyConsumptionLog);
        return getDataTable(list);
    }



    /**
     * 获取次数消耗日志详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbFrequencyConsumptionLogService.selectTbFrequencyConsumptionLogByIdsys(id));
    }

    /**
     * 新增次数消耗日志
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:sys:add')")
    @Log(title = "次数消耗日志", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return toAjax(tbFrequencyConsumptionLogService.insertTbFrequencyConsumptionLogsys(tbFrequencyConsumptionLog));
    }

    /**
     * 修改次数消耗日志   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:sys:edit')")
    @Log(title = "次数消耗日志", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbFrequencyConsumptionLog tbFrequencyConsumptionLog)
    {
        return toAjax(tbFrequencyConsumptionLogService.updateTbFrequencyConsumptionLogsys(tbFrequencyConsumptionLog));
    }

    /**
     * 删除次数消耗日志   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:frequentcylog:sys:remove')")
    @Log(title = "次数消耗日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbFrequencyConsumptionLogService.deleteTbFrequencyConsumptionLogByIdssys(ids));
    }






}
