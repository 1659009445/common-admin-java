# 使用

配置:
* 数据库 url database
* redis
* jwt

快速上手:
* 新增接口:modules/pack_name
* 接口配置权限@PreAuthorize("hasAuthority('xx')")
* 更改权限需要在src中进行

前端增加新功能:

* 菜单管理页添加新菜单,菜单若需展示需配置component(组件)和url(路径),标题项需启用
* 创建相应url下的vue文件,需和component保持一致
* router.index配置相应路由
* 角色菜单下为对应角色分配相应菜单权限
