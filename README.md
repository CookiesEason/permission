# permission
后台权限管理系统 采用了springboot2,mybatis,以及redis。权限管理系统==>包含了权限,权限拦截(重点),角色,用户,部门添加更新等等基础功能。
<br>
前端界面由vue实现 <a href="https://github.com/CookiesEason/vue-permission">vue-permission</a>
### 部门模块 <hr>
采用树形结构
例如 x部门--> x1部门，x2部门。x2部门也可以有x3部门。
**<p>实现方式是字段加入level,第一级为0，第二级0.1，第三级0.1.1以此类推。</p>**
