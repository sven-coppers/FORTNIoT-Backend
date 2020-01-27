<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Intelligible IoT Evaluation Control Panel</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Intelligible IoT Control Panel</h1>
    <div class="block">
        <h2>Use case</h2>
        <table>
            <thead>
            <tr>
                <td>Rules</td>
                <td>Devices</td>
                <td>States</td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <select id="rule_set" size="5">
                        <option value="rule_set_1">Set 1</option>
                        <option value="rule_set_2">Set 2</option>
                        <option value="rule_set_3">Set 3</option>
                        <option value="rule_set_4">Set 4</option>
                        <option value="rule_set_5">Set 5</option>
                    </select>
                </td>
                <td>
                    <select id="device_set" size="5">
                        <option value="device_set_1">Set 1</option>
                        <option value="device_set_2">Set 2</option>
                        <option value="device_set_3">Set 3</option>
                        <option value="device_set_4">Set 4</option>
                        <option value="device_set_5">Set 5</option>
                    </select>
                </td>
                <td>
                    <select id="state_set" size="5">
                        <option value="state_set_1">Set 1</option>
                        <option value="state_set_2">Set 2</option>
                        <option value="state_set_3">Set 3</option>
                        <option value="state_set_4">Set 4</option>
                        <option value="state_set_5">Set 5</option>
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
        <button id="apply">Apply</button>
    </div>

    <div class="block">
        <h2>Hassio Instance</h2>
        <p>Connect to an existing Hassio Instance to start listening to actual IoT environment</p>
        <input type="text" value="http://hassio.local:8123/api/" />
        <button id="start_listening">Start Listening</button>
        <button id="stop_listening">Stop Listening</button>
    </div>

    <div class="block">
        <h2>Devices</h2>
        <table>
            <thead>
            <tr>
                <td class="checkbox_cell">Enabled</td>
                <td class="checkbox_cell">Available to user</td>
                <td>Identifier</td>
                <td>Friendly Name</td>
            </tr>
            </thead>
            <tbody id="table_body_devices"></tbody>
        </table>
    </div>

    <div class="block">
        <h2>Rules</h2>
        <table>
            <thead>
                <tr>
                    <td class="checkbox_cell">Enabled</td>
                    <td class="checkbox_cell">Available to user</td>
                    <td>Identifier</td>
                    <td>Trigger</td>
                    <td>Actions</td>
                </tr>
            </thead>
            <tbody id="table_body_rules"></tbody>
        </table>

        <div class="block">
            <h2>States</h2>
            <table>
                <thead>
                <tr>
                    <td>Device</td>
                    <td>State</td>
                    <td>Last CHANGED</td>
                    <td>Last UPDATED</td>
                    <!--<td>Atrributes</td> -->
                </tr>
                </thead>
                <tbody id="table_body_states"></tbody>
            </table>
        </div>

  <!--      <h2>Device sets</h2>
        <p>
            <input type="radio" name="scenario" id="devices_simple" value="simple"><label for="scenario_simple">Simple</label>
            <input type="radio" name="scenario" id="scenario_full" value="full"><label for="scenario_full">Full Blown</label>
        </p>

        <h2>Scenario</h2>
        <p>
            <input type="radio" name="scenario" id="scenario_simple" value="simple"><label for="scenario_simple">Simple</label>
            <input type="radio" name="scenario" id="scenario_full" value="full"><label for="scenario_full">Full Blown</label>
        </p>
        <h2>Predictions</h2>
        <p>
            <input type="radio" name="predictions" id="predictions_off" value="off"><label for="predictions_off">Off</label>
            <input type="radio" name="predictions" id="predictions_on" value="on"><label for="predictions_on">On</label>
        </p>
        <button type="submit">Apply</button> -->
    </div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="js/study.js"></script>
</html>