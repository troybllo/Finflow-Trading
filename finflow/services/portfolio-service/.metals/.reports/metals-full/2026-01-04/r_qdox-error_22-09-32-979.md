error id: file://<WORKSPACE>/src/main/java/com/finflow/portfolio/api/PortfolioController.java
file://<WORKSPACE>/src/main/java/com/finflow/portfolio/api/PortfolioController.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[51,1]

error in qdox parser
file content:
```java
offset: 1840
uri: file://<WORKSPACE>/src/main/java/com/finflow/portfolio/api/PortfolioController.java
text:
```scala
package com.finflow.portfolio.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finflow.portfolio.application.PortfolioService;
import com.finflow.portfolio.dto.HoldingRequest;
import com.finflow.portfolio.dto.HoldingResponse;
import com.finflow.portfolio.dto.PortfolioResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/portfolio/")
public class PortfolioController {
  private final PortfolioService portfolioService;

  public PortfolioController(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  @GetMapping("{userId}")
  public ResponseEntity<PortfolioResponse> getUserPortfolio(@PathVariable String userId) {
    PortfolioResponse portfolio = portfolioService.getUserPortfolio(userId);

    return ResponseEntity.ok(portfolio);
  }

  @PostMapping("{userId}/holdings")
  public ResponseEntity<HoldingResponse> addUserHolding(@PathVariable String userId,
      @Valid @RequestBody HoldingRequest request) {
    HoldingResponse holding = portfolioService.addUserHolding(userId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(holding);
  }

  @GetMapping("{userId}/holdings")
	public ResponseEntity<List<HoldingResponse>> listUserHoldings(@PathVariable String userId)   {
  	List<HoldingResponse> holdings = portfolioService.listUserHoldings(userId);

		return ResponseEntity.ok(holdings);
  }
@@
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:39)
	scala.meta.internal.mtags.Mtags$.allToplevels(Mtags.scala:150)
	scala.meta.internal.metals.DefinitionProvider.fromMtags(DefinitionProvider.scala:365)
	scala.meta.internal.metals.DefinitionProvider.$anonfun$positionOccurrence$4(DefinitionProvider.scala:284)
	scala.Option.orElse(Option.scala:477)
	scala.meta.internal.metals.DefinitionProvider.$anonfun$positionOccurrence$1(DefinitionProvider.scala:284)
	scala.Option.flatMap(Option.scala:283)
	scala.meta.internal.metals.DefinitionProvider.positionOccurrence(DefinitionProvider.scala:276)
	scala.meta.internal.metals.JavaDocumentHighlightProvider.$anonfun$documentHighlight$1(JavaDocumentHighlightProvider.scala:26)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.metals.JavaDocumentHighlightProvider.documentHighlight(JavaDocumentHighlightProvider.scala:22)
	scala.meta.internal.metals.MetalsLspService.$anonfun$documentHighlights$1(MetalsLspService.scala:995)
	scala.meta.internal.metals.CancelTokens$.$anonfun$apply$2(CancelTokens.scala:26)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1575)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/src/main/java/com/finflow/portfolio/api/PortfolioController.java