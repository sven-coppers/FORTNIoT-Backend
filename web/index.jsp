<html>
  <head>
    <title>Fortunettes for IoT</title>
    <link type="text/css" rel="stylesheet" href="study/css/style.css">
  </head>
  <body>
  <div class="block">
    <h1>Endpoints Sven</h1>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/states/">states/</a> Get the current state of all devices</li>
      <li><span class="method">GET</span> <a class="uri" href="api/states/stream">states/stream</a> Get live state updates state of all devices</li>
      <li><span class="method">GET</span> <a class="uri" href="api/states/history">states/history</a> Get the historical state of all devices</li>
      <li><span class="method">GET</span> <a class="uri" href="api/states/history/sun.sun">states/history/{id}</a> Get the historical states of the device with {id}, e.g. 'sun.sun'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/states/future">states/future</a> Get the future state of all devices</li>
      <li><span class="method">GET</span> <a class="uri" href="api/states/future/sun.sun">states/future/{id}</a> Get the future states of the device with {id}, e.g. 'sun.sun'</li>
      <li><span class="method">POST</span> <a class="uri" href="api/states/future/simulate">states/future/simulate</a> Simulate an alternative future based on the input, returns only the alternative future</li>
    </ul>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/rules">rules/</a> Get an overview of all rules registered in the system</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/text">rules/text/</a> Get an overview of all rules registered in the system in plain text</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/history">rules/history</a> Get the historical executions of all rules</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/history/'rule.sun_rise">rules/history/{id}</a> Get the historical executions of the rule with {id}, e.g. 'rule.sun_rise'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/future">rules/future</a> Get the future executions of a rule</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/future/rule.sun_set">rules/future/{id}</a> Get the future executions of the rule with {id}, e.g. 'rule.sun_set'</li>
      <li><span class="method">PUT</span> <a class="uri" href="api/rules/'rule.sun_rise">rules/{id}/</a> Set the state of a rule {enabled: true}</li>
    </ul>
    <p>Do you want to perform the <a class="button" href="study/">study</a>?</p>
  </div>

  <div class="block">
    <h1>Endpoints Bram</h1>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/bram/">bram/</a> Hello, world</li>
    </ul>
  </div>

  <div class="block">
    <h1>Endpoints Mathias</h1>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/">mathias/</a> Hello, world</li>
    </ul>
    </div>
  </body>
</html>