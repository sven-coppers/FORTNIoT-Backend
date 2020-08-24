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
      <li><span class="method">PUT</span> <a class="uri" href="api/rules">rules/</a> Set the state of a rule {enabled: true, available: true}</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/text">rules/text/</a> Get an overview of all rules registered in the system in plain text</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/history">rules/history</a> Get the historical executions of all rules</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/history/'rule.sun_rise">rules/history/{id}</a> Get the historical executions of the rule with {id}, e.g. 'rule.sun_rise'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/future">rules/future</a> Get the future executions of a rule</li>
      <li><span class="method">GET</span> <a class="uri" href="api/rules/future/rule.sun_set">rules/future/{id}</a> Get the future executions of the rule with {id}, e.g. 'rule.sun_set'</li>
      <li><span class="method">PUT</span> <a class="uri" href="api/rules/'rule.sun_rise">rules/{id}/</a> Set the state of a rule {enabled: true, available: true}</li>
    </ul>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/solutions">solutions/</a> Get all solutions</li>
    </ul>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/config">config/</a> Get the system configuration</li>
      <li><span class="method">PUT</span> <a class="uri" href="api/config">config/</a> Set the system configuration </li>
      <li><span class="method">GET</span> <a class="uri" href="api/config/predictions">config/predictions/</a> Check if the prediction engine is on</li>
      <li><span class="method">PUT</span> <a class="uri" href="api/config/predictions">config/predictions/</a> Turn the prediction engine on/off</li>
      <li><span class="method">GET</span> <a class="uri" href="api/study/case">study/case/</a> Get the current use case of the study</li>
      <li><span class="method">PUT</span> <a class="uri" href="api/study/case/">study/case/</a> Set the current use case of the study</li>
    </ul>
    <p>Do you want to perform the <a class="button" href="study/">study</a>?</p>
  </div>

  <div class="block">
    <h1>Endpoints Bram</h1>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/bram/">bram/</a> Hello, world</li>
      <li><span class="method">GET</span> <a class="uri" href="api/bram/devices">bram/devices</a> Get the id and friendly name of all the devices</li>
      <li><span class="method">GET</span> <a class="uri" href="api/bram/devices/light.living_standing_lamp">bram/devices/{id}</a> Get the id and friendly name of a particular device</li>
      <li><span class="method">GET</span> <a class="uri" href="api/bram/why/light.hue_color_lamp_2">bram/why/{id}</a> Get the reason of the state of a entity</li>
    </ul>
  </div>

  <div class="block">
    <h1>Endpoints Mathias</h1>
    <ul>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/">mathias/</a> Hello, world</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/future/">mathias/future/</a> Get the future</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/actions/">mathias/actions/</a> Get all actions</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/actions/actionId1">mathias/actions/{id}</a> Get the action with {id}, e.g. 'actionId1'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/rules/rule.sun_rise_lights">mathias/rules/{id}</a> Get the rule with {id}, e.g. 'rule.sun_rise_lights'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/devices/actions/light.living_standing_lamp">mathias/devices/actions/{id}</a> Get the actions for the device with {id}, e.g. 'light.living_standing_lamp'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/conflicts/future">mathias/conflicts/future</a> Get the future conflicts</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/conflicts/future/light.hue_color_spot_2">mathias/conflicts/future/{id}</a> Get the future conflicts of the device with {id}, e.g. 'light.hue_color_spot_2'</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/solutions/future">mathias/solutions/future</a> Get the future conflict solutions</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/solutions/future/light.hue_color_spot_2">mathias/solutions/future/{id}</a> Get the future conflict solutions for the device with {id}, e.g. 'light.hue_color_spot_2'</li>
      <li><span class="method">POST</span> <a class="uri" href="api/mathias/solutions">mathias/solutions</a> Set solution for conflicts</li>
      <li><span class="method">POST</span> <a class="uri" href="api/mathias/solutions/remove">mathias/solutions/remove</a> Remove solution for conflicts</li>
      <li><span class="method">POST</span> <a class="uri" href="api/mathias/solutions/update">mathias/solutions/update</a> Update solution for conflicts</li>
      <li><span class="method">GET</span> <a class="uri" href="api/mathias/solutions/clean">mathias/solutions/clean</a> Clean solutions</li>
    </ul>
    </div>
  </body>
</html>