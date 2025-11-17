CONFIG_FILE="$HOME/config/config.properties"

DB_USER=$(grep '^hsqldb.user='     "$CONFIG_FILE" | cut -d'=' -f2-)
DB_PASS=$(grep '^hsqldb.password=' "$CONFIG_FILE" | cut -d'=' -f2-)
HSQLDB_JAR=$(grep '^hsqldb.jar='      "$CONFIG_FILE" | cut -d'=' -f2-)
DB_PATH=$(grep '^hsqldb.db_path='     "$CONFIG_FILE" | cut -d'=' -f2-)
DB_NAME=$(grep '^hsqldb.db_name='     "$CONFIG_FILE" | cut -d'=' -f2-)
HSQL_PORT=$(grep '^hsqldb.port='      "$CONFIG_FILE" | cut -d'=' -f2-)
HSQL_ADDR=$(grep '^hsqldb.address='   "$CONFIG_FILE" | cut -d'=' -f2-)

java -cp "$HSQLDB_JAR" org.hsqldb.server.Server \
  --database.0 "file:${DB_PATH}" \
  --dbname.0   "$DB_NAME" \
  --port       "$HSQL_PORT" \
  --address    "$HSQL_ADDR" &


Y la GUI:

java -cp "$HSQLDB_JAR" org.hsqldb.util.DatabaseManagerSwing \
  --url "jdbc:hsqldb:hsql://localhost:${HSQL_PORT}/${DB_NAME}" \
  --user "$DB_USER" \
  --password "$DB_PASS" &