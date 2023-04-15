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
import com.ruoyi.chatgpt.domain.TbAnsweUser;
import com.ruoyi.chatgpt.service.ITbAnsweUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * 小程序用户聊天列Controller
 *
 * @author ruoyi
 * @date 2023-02-16
 */
@RestController
@RequestMapping("/cricleai/user")
public class TbAnsweUserController extends BaseController
{
    @Autowired
    private ITbAnsweUserService tbAnsweUserService;

    /**
     * 查询小程序用户聊天列列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbAnsweUser tbAnsweUser)
    {
        Long userId = SecurityUtils.getUserId();
//        tbAnsweUser.setUserId(userId);
        startPage();
        List<TbAnsweUser> list = tbAnsweUserService.selectTbAnsweUserList(tbAnsweUser);
        return getDataTable(list);
    }

    /**
     * 导出小程序用户聊天列列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:export')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbAnsweUser tbAnsweUser)
    {
        List<TbAnsweUser> list = tbAnsweUserService.selectTbAnsweUserList(tbAnsweUser);
        ExcelUtil<TbAnsweUser> util = new ExcelUtil<TbAnsweUser>(TbAnsweUser.class);
        util.exportExcel(response, list, "小程序用户聊天列数据");
    }

    /**
     * 获取小程序用户聊天列详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:query')")
    @GetMapping(value = "/{answeUserId}")
    public AjaxResult getInfo(@PathVariable("answeUserId") Long answeUserId)
    {
        return success(tbAnsweUserService.selectTbAnsweUserByAnsweUserId(answeUserId));
    }

    /**
     * 新增小程序用户聊天列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:add')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbAnsweUser tbAnsweUser)
    {
        return toAjax(tbAnsweUserService.insertTbAnsweUser(tbAnsweUser));
    }

    /**
     * 修改小程序用户聊天列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:edit')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbAnsweUser tbAnsweUser)
    {
        return toAjax(tbAnsweUserService.updateTbAnsweUser(tbAnsweUser));
    }

    /**
     * 删除小程序用户聊天列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:remove')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{answeUserIds}")
    public AjaxResult remove(@PathVariable Long[] answeUserIds)
    {
        return toAjax(tbAnsweUserService.deleteTbAnsweUserByAnsweUserIds(answeUserIds));
    }






/**
 * 查询小程序用户聊天列列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:user:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbAnsweUser tbAnsweUser)
    {
        startPage();
        List<TbAnsweUser> list = tbAnsweUserService.selectTbAnsweUserListsys(tbAnsweUser);
        return getDataTable(list);
    }



    /**
     * 获取小程序用户聊天列详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:sys:query')")
    @GetMapping(value = "/sys/{answeUserId}")
    public AjaxResult getInfosys(@PathVariable("answeUserId") Long answeUserId)
    {
        return success(tbAnsweUserService.selectTbAnsweUserByAnsweUserIdsys(answeUserId));
    }

    /**
     * 新增小程序用户聊天列
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:sys:add')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbAnsweUser tbAnsweUser)
    {
        return toAjax(tbAnsweUserService.insertTbAnsweUsersys(tbAnsweUser));
    }

    /**
     * 修改小程序用户聊天列   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:sys:edit')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbAnsweUser tbAnsweUser)
    {
        return toAjax(tbAnsweUserService.updateTbAnsweUsersys(tbAnsweUser));
    }

    /**
     * 删除小程序用户聊天列   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:user:sys:remove')")
    @Log(title = "小程序用户聊天列", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{answeUserIds}")
    public AjaxResult removesys(@PathVariable Long[] answeUserIds)
    {
        return toAjax(tbAnsweUserService.deleteTbAnsweUserByAnsweUserIdssys(answeUserIds));
    }






}
