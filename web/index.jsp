<html>
  <head>
    <title>Fortunettes for IoT</title>
    <style>
      a {
        display: inline-block;
        width: 150px;
      }
    </style>
  </head>
  <body>
    <h1>Endpoints Sven</h1>
    <ul>
      <li>GET <a href="api/states/">states/</a> Get the current state of all devices</li>
      <li>GET <a href="api/states/history">states/history</a> Get the historical state of all devices</li>
      <li>GET <a href="api/states/history/sun.sun">states/history/{id}</a> Get the historical states of the device with {id}, e.g. 'sun.sun'</li>
      <li>GET <a href="api/states/future">states/future</a> Get the future state of all devices</li>
      <li>GET <a href="api/states/future/sun.sun">states/future/{id}</a> Get the future states of the device with {id}, e.g. 'sun.sun'</li>
      <li>GET <a href="api/states/stream">states/stream</a> Get live state updates state of all devices</li>
    </ul>
    <ul>
      <li>GET <a href="api/rules">rules/</a> Get an overview of all rules registered in the system</li>
      <li>GET <a href="api/rules/history">rules/history</a> Get the historical executions of all rules</li>
      <li>GET <a href="api/rules/history/'rule.sun_rise">rules/history/{id}</a> Get the historical executions of the rule with {id}, e.g. 'rule.sun_rise'</li>
      <li>GET <a href="api/rules/future">rules/future</a> Get the future executions of a rule</li>
      <li>GET <a href="api/rules/future/rule.sun_set">rules/future/{id}</a> Get the future executions of the rule with {id}, e.g. 'rule.sun_set'</li>
    </ul>

    <h1>Endpoints Bram</h1>
    <ul>
      <li>GET <a href="api/bram/">bram/</a> Hello, world</li>
    </ul>

    <h1>Endpoints Mathias</h1>
    <ul>
      <li>GET <a href="api/mathias/">mathias/</a> Hello, world</li>
    </ul>
  </body>
</html>