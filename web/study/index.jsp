<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Intelligible IoT Evaluation Control Panel</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>Intelligible IoT Control Panel</h1>

    <form method="GET" action="" class="block">
        <h2>Devices</h2>

        <h2>Rules</h2>

        <table>
            <thead>
                <tr>
                    <td>Identifier</td>
                    <td>Trigger</td>
                    <td>Actions</td>
                    <td class="checkbox_cell">Enabled</td>
                    <td class="checkbox_cell">Available to user</td>
                </tr>
            </thead>
            <tbody id="table_body_rules"></tbody>
        </table>

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
        <button type="submit">Apply</button>
    </form>

</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="js/study.js"></script>
</html>