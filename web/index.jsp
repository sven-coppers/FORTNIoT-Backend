<html>
  <head>
    <title>Fortunettes for IoT</title>
    <style>
      a {
        display: inline-block;
        font-family: "Courier New";
        width: 250px;
      }

      .method {
        width: 60px;
        font-family: "Courier New";
        display: inline-block;
      }
    </style>
  </head>
  <body>
    <h1>Endpoints Sven</h1>
    <ul>
      <li><span class="method">GET</span> <a href="api/states/">states/</a> Get the current state of all devices</li>
      <li><span class="method">GET</span> <a href="api/states/stream">states/stream</a> Get live state updates state of all devices</li>
      <li><span class="method">GET</span> <a href="api/states/history">states/history</a> Get the historical state of all devices</li>
      <li><span class="method">GET</span> <a href="api/states/history/sun.sun">states/history/{id}</a> Get the historical states of the device with {id}, e.g. 'sun.sun'</li>
      <li><span class="method">GET</span> <a href="api/states/future">states/future</a> Get the future state of all devices</li>
      <li><span class="method">GET</span> <a href="api/states/future/sun.sun">states/future/{id}</a> Get the future states of the device with {id}, e.g. 'sun.sun'</li>
      <li><span class="method">POST</span> <a href="api/states/future/simulate">states/future/simulate</a> Simulate an alternative future based on the input, returns only the alternative future</li>
    </ul>
    <ul>
      <li><span class="method">GET</span> <a href="api/rules">rules/</a> Get an overview of all rules registered in the system</li>
      <li><span class="method">GET</span> <a href="api/rules/text">rules/text/</a> Get an overview of all rules registered in the system in plain text</li>
      <li><span class="method">GET</span> <a href="api/rules/history">rules/history</a> Get the historical executions of all rules</li>
      <li><span class="method">GET</span> <a href="api/rules/history/'rule.sun_rise">rules/history/{id}</a> Get the historical executions of the rule with {id}, e.g. 'rule.sun_rise'</li>
      <li><span class="method">GET</span> <a href="api/rules/future">rules/future</a> Get the future executions of a rule</li>
      <li><span class="method">GET</span> <a href="api/rules/future/rule.sun_set">rules/future/{id}</a> Get the future executions of the rule with {id}, e.g. 'rule.sun_set'</li>
      <li><span class="method">PUT</span> <a href="api/rules/'rule.sun_rise">rules/{id}/</a> Set the state of a rule {enabled: true}</li>
    </ul>

    <h1>Endpoints Bram</h1>
    <ul>
      <li><span class="method">GET</span> <a href="api/bram/">bram/</a> Hello, world</li>
    </ul>

    <h1>Endpoints Mathias</h1>
    <ul>
      <li><span class="method">GET</span> <a href="api/mathias/">mathias/</a> Hello, world</li>
    </ul>
  </body>
</html>