package com.ruoyi.chatgpt.controller;

import java.util.*;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.Gpt35TurboVO;
import com.ruoyi.common.utils.json.JsonUtil;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.ruoyi.chatgpt.domain.TbKeyManager;
import com.ruoyi.chatgpt.service.ITbKeyManagerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
/**
 * key管理Controller
 *
 * @author ruoyi
 * @date 2023-03-06
 */
@RestController
@RequestMapping("/chatgpt/manager")
public class TbKeyManagerController extends BaseController
{
    @Autowired
    private ITbKeyManagerService tbKeyManagerService;

    /**
     * 查询key管理列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbKeyManager tbKeyManager)
    {
        Long userId = SecurityUtils.getUserId();
        tbKeyManager.setUserId(userId);
        startPage();
        List<TbKeyManager> list = tbKeyManagerService.selectTbKeyManagerList(tbKeyManager);
        return getDataTable(list);
    }

    /**
     * 导出key管理列表
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:export')")
    @Log(title = "key管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbKeyManager tbKeyManager)
    {
        List<TbKeyManager> list = tbKeyManagerService.selectTbKeyManagerList(tbKeyManager);
        ExcelUtil<TbKeyManager> util = new ExcelUtil<TbKeyManager>(TbKeyManager.class);
        util.exportExcel(response, list, "key管理数据");
    }

    /**
     * 获取key管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbKeyManagerService.selectTbKeyManagerById(id));
    }

    /**
     * 新增key管理
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:add')")
    @Log(title = "key管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbKeyManager tbKeyManager)
    {
        if(StrUtil.isBlank(tbKeyManager.getSecretKey())){
            return error("密钥不可为空");
        }

        Object apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url = "https://api.openai.com/v1/chat/completions";

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        try {
            String result2 = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                    .header("Content-Type", "application/json")
                    .body(postData)//表单内容
                    .timeout(200000)//超时，毫秒
                    .execute().body();
            if (StrUtil.isNotBlank(result2)) {
                String error = JsonUtil.parseMiddleData(result2, "error");
                String type = JsonUtil.parseMiddleData(error, "type");
                if (StrUtil.equals(type, "insufficient_quota")) {
                    return error("key次数用完");
                }
            }
            if (StrUtil.isNotBlank(result2)) {
                if (StrUtil.contains(result2, "invalid_request_error") ) {
                    String error = JsonUtil.parseMiddleData(result2, "error");
                    String type = JsonUtil.parseMiddleData(error, "type");
                    String code = JsonUtil.parseMiddleData(error, "code");
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"account_deactivated")){
                        return error("账户被封禁");
                    }
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"invalid_api_key")) {
                        return error("key不正确");
                    }
                }

                if (StrUtil.contains(result2, "statusCode") && StrUtil.contains(result2, "TooManyRequests")) {
                    return error("key次数用完");
                }
                if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code"))) {
                    return error("key次数用完");
                }else {
//                            return error("我没有搜索出来答案");
                }
            }
        }catch (Exception e){
            return error("key校验失败");
        }
        return toAjax(tbKeyManagerService.insertTbKeyManager(tbKeyManager));
    }

    /**
     * 修改key管理
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:edit')")
    @Log(title = "key管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbKeyManager tbKeyManager)
    {
        if(StrUtil.isBlank(tbKeyManager.getSecretKey())){
            return error("密钥不可为空");
        }

        Object apikey = tbKeyManager.getSecretKey();
        String input = "1加1等于几";
        //请求URL
        String url = "https://api.openai.com/v1/chat/completions";

        Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
        gpt35TurboVO.setRole("user");
        gpt35TurboVO.setContent(input);
        List<Gpt35TurboVO> objects = new ArrayList<>();
        objects.add(gpt35TurboVO);

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("model", "gpt-3.5-turbo");
        objectObjectHashMap.put("messages", objects);
        String postData = JSONUtil.toJsonStr(objectObjectHashMap);
        try {
            String result2 = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + apikey)//头信息，多个头信息多次调用此方法即可
                    .header("Content-Type", "application/json")
                    .body(postData)//表单内容
                    .timeout(200000)//超时，毫秒
                    .execute().body();
            if (StrUtil.isNotBlank(result2)) {
                String error = JsonUtil.parseMiddleData(result2, "error");
                String type = JsonUtil.parseMiddleData(error, "type");
                if (StrUtil.equals(type, "insufficient_quota")) {
                    return error("key次数用完");
                }
            }
            if (StrUtil.isNotBlank(result2)) {

                if (StrUtil.contains(result2, "invalid_request_error") ) {
                    String error = JsonUtil.parseMiddleData(result2, "error");
                    String type = JsonUtil.parseMiddleData(error, "type");
                    String code = JsonUtil.parseMiddleData(error, "code");
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"account_deactivated")){
                        return error("账户被封禁");
                    }
                    if (StrUtil.equals(type,"invalid_request_error") && StrUtil.equals(code,"invalid_api_key")) {
                        return error("key不正确");
                    }
                }

                if (StrUtil.contains(result2, "statusCode") && StrUtil.contains(result2, "TooManyRequests")) {
                    return error("key次数用完");
                }
                if (StrUtil.contains(result2, "code") && Objects.isNull(JsonUtil.parseMiddleData(result2, "code"))) {
                    return error("key次数用完");
                }else {
//                            return error("我没有搜索出来答案");
                }
            }
        }catch (Exception e){
            return error("key校验失败");
        }
        return toAjax(tbKeyManagerService.updateTbKeyManager(tbKeyManager));
    }


    /**
     * 删除key管理
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:remove')")
    @Log(title = "key管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbKeyManagerService.deleteTbKeyManagerByIdssys(ids));
    }



    /**
 * 查询key管理列表   -----   管理员使用
 */
@PreAuthorize("@ss.hasPermi('chatgpt:manager:sys:list')")
@GetMapping("/sys/list")
    public TableDataInfo listsys(TbKeyManager tbKeyManager)
    {
        startPage();
        List<TbKeyManager> list = tbKeyManagerService.selectTbKeyManagerListsys(tbKeyManager);
        return getDataTable(list);
    }



    /**
     * 获取key管理详细信息   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:sys:query')")
    @GetMapping(value = "/sys/{id}")
    public AjaxResult getInfosys(@PathVariable("id") Long id)
    {
        return success(tbKeyManagerService.selectTbKeyManagerByIdsys(id));
    }

    /**
     * 新增key管理
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:sys:add')")
    @Log(title = "key管理", businessType = BusinessType.INSERT)
    @PostMapping("/sys")
    public AjaxResult addsys(@RequestBody TbKeyManager tbKeyManager)
    {
        return toAjax(tbKeyManagerService.insertTbKeyManagersys(tbKeyManager));
    }

    /**
     * 修改key管理   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:sys:edit')")
    @Log(title = "key管理", businessType = BusinessType.UPDATE)
    @PutMapping("/sys")
    public AjaxResult editsys(@RequestBody TbKeyManager tbKeyManager)
    {
        return toAjax(tbKeyManagerService.updateTbKeyManagersys(tbKeyManager));
    }

    /**
     * 删除key管理   -----   管理员使用
     */
    @PreAuthorize("@ss.hasPermi('chatgpt:manager:sys:remove')")
    @Log(title = "key管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/sys/{ids}")
    public AjaxResult removesys(@PathVariable Long[] ids)
    {
        return toAjax(tbKeyManagerService.deleteTbKeyManagerByIdssys(ids));
    }



}
