REM La directory dove sono presenti i jar del generatore csi-swagger
set CSI_SWAGGER_GEN_HOME=c:\devtools\csi-swagger-codegen

set CLI_JAR=%CSI_SWAGGER_GEN_HOME%\swagger-codegen-cli.jar
set CUSTOM_GEN_JAR=%CSI_SWAGGER_GEN_HOME%\csi-java-swagger-codegen-1.0.0.jar

set SWAGGER_CP=%CLI_JAR%;%CUSTOM_GEN_JAR%

REM impostare solo per debug del generatore
set DEBUG_OPTS=
rem set DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=9797,server=y,suspend=y


set YAML_FILE=..\conf\yaml\src_yaml_demografia.yaml
set OUT_PATH=..\build\swagger-codegen\demografia
set CONF_JSON=swagger_config_java_demografia.json

rem pause "#### generazione skeleton jaxrs ####"
java -cp %SWAGGER_CP% %DEBUG_OPTS% io.swagger.codegen.SwaggerCodegen generate -i %YAML_FILE% -l jaxrs-resteasy-eap-csi -o %OUT_PATH% --config %CONF_JSON% 
