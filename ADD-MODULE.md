## English (US)
1. `Right click` on the root project "Kessoku-Lib", select New->Module in the context menu, create "package-common", "package-fabric "package-common", "package-fabric", "package-neo" (`package` refers to the module name);
2. Copy and paste the gradle script files under [`gradle/example`](./gradle/example) to their respective modules (`common` to `package-common`, `fabric` to `package-fabric`, and `neo` to `package-neo`);
3. Check that `settings.gradle` has added the modules you just added, and it's fine if it hasn't;
4. Synchronizing the gradle
5. If you want to add module dependencies use `moduleImplementation(project("module-name-common"))`.
6. Finally, add include to `fabric` and `neo`.

## Simplified Chinese
1. 在根项目“Kessoku-Lib”上`右键`，在右键菜单中选择`新建->模块`, 新建“package-common”，“package-fabric”，“package-neo”（`package`指的是模块名称）；
2. 复制粘贴 [`gradle/example`](./gradle/example) 下的gradle脚本文件到各自所属模块（common复制common文件夹内的到`package-common`模块，fabric复制fabric文件夹内的到`package-fabric`，neo复制neo文件夹内的到`package-neo`）；
3. 检查 `settings.gradle` 是否添加了刚才的模块，没有添加即可；
4. 同步gradle
5. 如果要添加模块依赖请用`moduleImplementation(project("模块名-common"))`
6. 最后在`fabric`和`neo`添加include（请用`moduleInclude(project("模块名-modloader"))`）
