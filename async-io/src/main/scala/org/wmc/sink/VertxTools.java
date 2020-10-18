package org.wmc.sink;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

/**
 * @author: WangMC
 * @date: 2020/10/18 12:25
 * @description:
 */
public class VertxTools {

    public static PgPool getConnection(String host, int port, String database, String user, String Password) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(port)
                .setHost(host)
                .setDatabase(database)
                .setUser(user)
                .setPassword(Password);

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        // Create the client pool
        PgPool client = PgPool.pool(connectOptions, poolOptions);
        return client;
    }


    public static void execSql(String psSql,String input, PgPool client) {

        client
                .preparedQuery(psSql)
                .execute(Tuple.of(input), ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        System.out.println(rows.rowCount());

                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }

                });
    }

}
