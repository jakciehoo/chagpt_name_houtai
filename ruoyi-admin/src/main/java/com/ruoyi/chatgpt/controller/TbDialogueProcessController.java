package com.ruoyi.chatgpt.controller;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.chatgpt.domain.TbDialogueMain;
import com.ruoyi.chatgpt.domain.TbModelTable;
import com.ruoyi.chatgpt.service.ITbDialogueMainService;
import com.ruoyi.chatgpt.service.ITbModelTableService;
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
import com.ruoyi.chatgpt.domain.TbDialogueProcess;
import com.ruoyi.chatgpt.service.ITbDialogueProcessService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 充值卡-从Controller
 *
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/cricleai/diglogueprocess")
public class TbDialogueProcessController extends BaseController
{
    @Autowired
    private ITbDialogueProcessService tbDialogueProcessService;

    @Autowired
    private ITbDialogueMainService tbDialogueMainService;

    @Autowired
    private ITbModelTableService iTbModelTableService;

    /**
     * 查询充值卡-从列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbDialogueProcess tbDialogueProcess)
    {
        Long userId = SecurityUtils.getUserId();
        tbDialogueProcess.setUserId(userId);
        startPage();
        List<TbDialogueProcess> list = tbDialogueProcessService.selectTbDialogueProcessList(tbDialogueProcess);
        return getDataTable(list);
    }

    /**
     * 导出充值卡-从列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:export')")
    @Log(title = "充值卡-从", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbDialogueProcess tbDialogueProcess)
    {
        List<TbDialogueProcess> list = tbDialogueProcessService.selectTbDialogueProcessList(tbDialogueProcess);
        ExcelUtil<TbDialogueProcess> util = new ExcelUtil<TbDialogueProcess>(TbDialogueProcess.class);
        util.exportExcel(response, list, "充值卡-从数据");
    }

    /**
     * 获取充值卡-从详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbDialogueProcessService.selectTbDialogueProcessById(id));
    }

    /**
     * 新增充值卡-从
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:add')")
    @Log(title = "充值卡-从", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbDialogueProcess tbDialogueProcess)
    {
        return toAjax(tbDialogueProcessService.insertTbDialogueProcess(tbDialogueProcess));
    }

    /**
     * 修改充值卡-从
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:edit')")
    @Log(title = "充值卡-从", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbDialogueProcess tbDialogueProcess)
    {
        return toAjax(tbDialogueProcessService.updateTbDialogueProcess(tbDialogueProcess));
    }

    /**
     * 删除充值卡-从
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:remove')")
    @Log(title = "充值卡-从", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbDialogueProcessService.deleteTbDialogueProcessByIds(ids));
    }






/**
 * 查询充值卡-从列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbDialogueProcess tbDialogueProcess)
    {
        startPage();
        List<TbDialogueProcess> list = tbDialogueProcessService.selectTbDialogueProcessListsys(tbDialogueProcess);
        return getDataTable(list);
    }



    /**
     * 获取充值卡-从详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbDialogueProcessService.selectTbDialogueProcessByIdsys(id));
    }

    /**
     * 新增充值卡-从
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:sys:add')")
    @Log(title = "充值卡-从", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbDialogueProcess tbDialogueProcess)
    {
        return toAjax(tbDialogueProcessService.insertTbDialogueProcesssys(tbDialogueProcess));
    }

    /**
     * 修改充值卡-从   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:sys:edit')")
    @Log(title = "充值卡-从", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbDialogueProcess tbDialogueProcess)
    {
        return toAjax(tbDialogueProcessService.updateTbDialogueProcesssys(tbDialogueProcess));
    }

    /**
     * 删除充值卡-从   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:diglogueprocess:sys:remove')")
    @Log(title = "充值卡-从", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbDialogueProcessService.deleteTbDialogueProcessByIdssys(ids));
    }






    /**
     * 获取对话记录
     * 此方法有点不太好,因为如果service放入实现层,会产生循环依赖
     */
    @PostMapping("/listDialogue")
    public TableDataInfo listDialogue(@RequestBody TbDialogueProcess tbDialogueProcess)
    {
        if (Objects.isNull(tbDialogueProcess.getSessionId())){
            throw new RuntimeException("会话主题为空");
        }
        startPage();
        tbDialogueProcess.setUserId(SecurityUtils.getUserId());
        List<TbDialogueProcess> list = tbDialogueProcessService.listDialogue(tbDialogueProcess);

        //添加角色信息
        TbDialogueMain tbDialogueMain = tbDialogueMainService.selectTbDialogueMainById(tbDialogueProcess.getSessionId());
        if (Objects.isNull(tbDialogueMain)){
            throw new RuntimeException("会话为空");
        }
        //获取模型ID
        Long dialogueRoleId = tbDialogueMain.getDialogueRoleId();
        TbModelTable tbModelTable = iTbModelTableService.selectTbModelTableById(dialogueRoleId);
        if (Objects.isNull(tbModelTable)){
            throw new RuntimeException("模型选择为空,请重新创建对话");
        }
        TbDialogueProcess tbDialogueProcess1 = new TbDialogueProcess();
        tbDialogueProcess1.setAnswerContent(tbModelTable.getModelContent());
        list.add(0,tbDialogueProcess1);
        return getDataTable(list);
    }


}
