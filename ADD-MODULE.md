1. 在根项目“Kessoku-Lib”上`右键`，在右键菜单中选择`新建->模块`, 新建“package-common”，“package-fabric”，“package-neo”（`package`指的是模块名称）；
2. 复制粘贴 [`gradle/example`](./gradle/example) 下的gradle脚本文件到各自所属模块(common复制common文件夹内的到`package-common`模块，fabric复制fabric文件夹内的到`package-fabric`，neo复制neo文件夹内的到`package-neo`)；
3. 检查 settings.gradle是否添加了刚才的模块，没有添加即可；
4. 同步gradle
5. 如果要添加模块依赖请用`moduleImplementation(project("模块名-common"))`
6. 最后在fabric和neo添加include