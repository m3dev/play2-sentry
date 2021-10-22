Play2向け Sentryログ出力サポート
=====

Play2においてSentryへのログ出力にHTTPリクエストの内容を記録させるためのモジュールです。  
Play2では、Java Servletとは違う独自の構造でHTTPリクエスト情報を保持しているため、それをSentry向けに変換する必要があります、
  
また、１リクエスト内でもマルチスレッドで処理することが多いため、スレッド内でのログにリクエスト情報を付与するためにはスレッド間でリクエスト情報を共有する必要があります。

これらに対処するため、本モジュールでは主に以下のことを行っています。

  * PlayのHTTPリクエスト情報をSentryに送信するためのフォーマット変換処理を実装
  * 本モジュールを有効化したSentryClientを生成するための独自SentryClientFactoryを定義
  * Playのリクエスト情報をスレッドローカル変数に保存するためのフィルタを定義
  * Akkaでのスレッド起動時に、HTTP情報を持つスレッドローカル変数とlogbackのMDCを引き継ぐようにする処理を追加

使い方
----

本モジュールの導入方法は以下のとおりです。

1. Playアプリの依存に追加する

```scala
libraryDependencies ++= Seq(
  "com.m3.play2" % "play2-sentry" % "1.0.0"
)
```

2. sentry設定でfactoryを指定する

**sentry.properties**
```properties
factory=com.m3.play2.sentry.PlaySentryFactory
```

3. Akkaのデフォルトdispatcherを本モジュールのクラスに指定する

**application.conf**

```hocon
play {
  akka {
    actor {
      default-dispatcher = {
        // Dispatcherを指定する
        type = "com.m3.play2.sentry.SentryDispatcherConfigurator"
      }
    }
  }
}
```

4. HTTPフィルタにSentryLoggingFilterを追加

```scala
import com.m3.play2.sentry.SentryLoggingFilter

class ApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  // filter を追加
  override lazy val httpFilters = Seq(
    new SentryLoggingFilter()
  )
}
```

5. Sentryの標準的な導入設定を行う

参考: https://docs.sentry.io/platforms/java/legacy/

   * ログ出力設定でSentryAppenderを指定する
   * アプリケーション固有のパッケージ名を明記
   * etc...


本モジュールの開発方法
----

### ビルド

```shell
sbt package
```

### ローカル環境での動作確認

1. モジュールをivyのローカルキャッシュにインストールする

```shell
sbt publishLocal
```

2. 動作確認用Playアプリで、resolvers にローカルキャッシュディレクトリを追加する

**build.sbt**

```scala
resolvers += "MyLocalIvy" at "file://"+Path.userHome.absolutePath+"/.ivy2/local"
```

3. アプリに組み込む

4. アプリを実行する


### デプロイ

`v` から始まる名前のタグをプッシュすると、GitHub Action により Central Repository に登録されます。
