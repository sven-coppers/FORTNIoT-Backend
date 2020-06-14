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
        <h2>Experiment</h2>
        <label>Participant: <input type="text" id="experiment_participant" value="P" /></label>
        <label>Group: <input type="text" id="experiment_group" value="" /></label>
        <button id="start_experiment">Start</button>
        <button class="next_experiment">Next</button>
        <!--<button id="stop_experiment">Stop</button> -->
        <table id="task_table">
            <thead>
            <tr>
                <td class="status"></td>
                <td class="identifier">Task</td>
                <td>Facilitator</td>
                <td>Participant</td>
            </tr>
            </thead>
            <tbody id="task_table_body">
            </tbody>
        </table>
    </div>
    <div class="block">
        <h2>Use case</h2>
        <table id="use_case_table">
            <thead>
            <tr>
                <td>Rules</td>
                <td>Devices</td>
                <td>States</td>
                <td>Preset</td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td id="rule_sets">
                </td>
                <td id="device_sets">
                </td>
                <td id="state_sets">
                </td>
                <td>
                    <select id="preset" size="25"></select>
                </td>
            </tr>
            </tbody>
        </table>
        <button id="apply">Apply</button>
        <button id="export">Export</button>
    </div>
    <div class="block">
        <h2>Predictions</h2>
        <button id="start_predicting">Start Predicting</button>
        <button id="stop_predicting">Stop Predicting</button>
        <button id="update_predictions">Update Predictions</button>
    </div>
    <div class="block">
        <h2>Hassio Instance</h2>
        <p>Connect to an existing Hassio Instance to start listening to actual IoT environment</p>
        <input type="text" value="http://hassio.local:8123/api/" />
        <button id="start_listening">Start Listening</button>
        <button id="stop_listening">Stop Listening</button>
       <!-- <button id="remove_devices">Remove All Devices</button> -->
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
    </div>

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
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="js/study.js"></script>
</html>