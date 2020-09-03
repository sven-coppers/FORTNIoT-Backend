$(document).ready(function() {
    refresh();
});

$(document).on('submit','form',function(){
    //applyRuleProperties();
    applyUseCase();
    return false;
});


$("#start_experiment").click(function() {
    startExperiment();
    $(this).attr('disabled', true);
});


$(".next_experiment").click(function() {
    nextStep();
});


$("#apply").click(function() {
    applyUseCase();
    $("#export").attr('disabled', false);
});

$("#export").click(function() {
    exportUseCase();
});

function exportUseCase() {
    let selection = $("select#preset").children("option:selected").val();

    $("#export").attr('disabled', true);

    $.ajax({
        url:            "/intelligibleIoT/api/config/export/" + selection + "/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        refresh();
        $("#export").attr('disabled', false);
    });
}

$("#update_predictions").click(function() {
    $.ajax({
        url:            "/intelligibleIoT/api/config/predictions/update/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        refresh();
    });
});

$("select#preset"). change(function(){
    applyPreset();
});

function refresh() {
    this.refreshDevices();
    this.refreshRules();
    this.refreshStates();
    this.refreshConfig();
    this.refreshUseCases();
    this.refreshPredictions();
    this.refreshStudy();
}

$("#start_listening").click(function() { applyConfig(true); });
$("#stop_listening").click(function() { applyConfig(false); });
$("#start_predicting").click(function() { applyPredictions(true); });
$("#stop_predicting").click(function() { applyPredictions(false); });

function applyUseCase() {
    let useCase = {
        rule_set: [],
        device_set: [],
        state_set: [],
        preset: null
    };

    $.each($("#rule_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.rule_set.push($(this).attr("id").replace("rule_set_", ""));
        }
    });

    $.each($("#device_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.device_set.push($(this).attr("id").replace("device_set_", ""));
        }
    });

    $.each($("#state_sets input"), function () {
        if($(this).prop('checked')) {
            useCase.state_set.push($(this).attr("id").replace("state_set_", ""));
        }
    });

    applyUseCaseSettings(useCase);
}

function applyPreset() {
    let useCase = {
        rule_set: [],
        device_set: [],
        state_set: [],
        preset: $("select#preset").children("option:selected").val()
    };

    applyUseCaseSettings(useCase);
}

function applyUseCaseSettings(settings) {
    $.ajax({
        url:            "/intelligibleIoT/api/study/case/",
        type:           "PUT",
        data: JSON.stringify(settings),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function refreshRules() {
    $.ajax({
        url:            "/intelligibleIoT/api/rules/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_rules").empty();

        for (let ruleID in data) {
            let rule = data[ruleID];
            let HTMLID = ruleIDToHTML(ruleID);

            $("#table_body_rules").append('<tr>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_enabled" id="' + HTMLID + '_enabled" value="' + HTMLID + '_enabled"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="rules_available" id="' + HTMLID + '_available" value="' + HTMLID + '_available"></td>\n' +
                '    <td>' + HTMLID + '</td>\n' +
                '    <td>' + rule["description"] + '</td>\n' +
                '    <td id="' + HTMLID + '_actions"></td>\n' +
                '</tr>');

            setRuleProperties(HTMLID, rule["enabled"], rule["available"]);

            for(let action of rule["actions"]) {
                $("#" + HTMLID + "_actions").append(action["description"] + "<br />");
            }
        }
    });
}

function refreshStates() {
    $.ajax({
        url:            "/intelligibleIoT/api/states/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_states").empty();

        for (let deviceID in data) {
            let deviceState = data[deviceID];

            if(deviceState == null) continue;

            $("#table_body_states").append('<tr>\n' +
                '    <td>' + deviceID + '</td>\n' +
                '    <td>' + deviceState["state"] + '</td>\n' +
                '    <td>' + dateToString(new Date(deviceState["last_changed"])) + '</td>\n' +
                '    <td>' + dateToString(new Date(deviceState["last_updated"]))+ '</td>\n' +
                //     '    <td>' + JSON.stringify(device["attributes"]).substring(0, 100) + '...</td>\n' +
                '</tr>');

            //  setRuleProperties(HTMLID, rule["enabled"], rule["available"]);
        }
    });
}

function refreshDevices() {
    $.ajax({
        url:            "/intelligibleIoT/api/bram/devices/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#table_body_devices").empty();

        for (let device of data) {
            if(device == null) continue;
            let HTMLID = deviceIDtoHTMLID(device["id"]);

            $("#table_body_devices").append('<tr>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="device_enabled" id="' + HTMLID + '_enabled" value="' + HTMLID + '_enabled"></td>\n' +
                '    <td class="checkbox_cell"><input type="checkbox" name="device_available" id="' + HTMLID + '_available" value="' + HTMLID + '_available"></td>\n' +
                '    <td>' + device["id"] + '</td>\n' +
                '    <td>' + device["friendly_name"] + '</td>\n' +
            '</tr>');

            setDeviceProperties(HTMLID, device["enabled"], device["available"]);
        }
    });
}

function refreshConfig() {
    $.ajax({
        url:            "/intelligibleIoT/api/config/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#start_listening").attr('disabled', data["connected_to_hassio"]);
        $("#stop_listening").attr('disabled', !data["connected_to_hassio"]);
    });
}

function refreshPredictions() {
    $.ajax({
        url:            "/intelligibleIoT/api/config/predictions",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#start_predicting").attr('disabled', data["predictions"]);
        $("#stop_predicting").attr('disabled', !data["predictions"]);
        $("#update_predictions").attr('disabled', !data["predictions"]);
    });
}

function refreshStudy() {
    $.ajax({
        url:            "/intelligibleIoT/api/study/steps/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#task_table_body").empty();

        for (let step of data) {
            let newRow = '';

            if(step["completed"]) {
                newRow += '<tr class="completed"><td class="status">&#10004;</td>';
            } else if(step["ongoing"]) {
                newRow += '<tr class="ongoing"><td class="status"><button class="next_experiment">Next</button></td>';
            } else {
                newRow += '<tr><td class="status"></td>';
            }

            newRow += '<td class="identifier">' + step["task_id"] + '</td>';

            // Empty for facilitator?
            if(step["for_participant"]) {
                newRow += '<td></td>';
            }

            newRow += '<td>' + step["instruction"];

            if(step["url"] != null && step["ongoing"]) {
                newRow += '<br /><a target="_blank" href="' + step["url"] + '">link</a>';
            }

            newRow += '</td>';

            // Empty for participant?
            if(!step["for_participant"]) {
                newRow += '<td></td>';
            }

            newRow += '</tr>';

            $("#task_table_body").append(newRow);
        }

        $("#task_table_body").find(".next_experiment").click(function() {
             nextStep();
         });
    });
}

function refreshUseCases() {
    $.ajax({
        url:            "/intelligibleIoT/api/study/case/",
        type:           "GET",
        headers: {
            Accept: "application/json; charset=utf-8" // FORCE THE JSON VERSION
        }
    }).done(function (data) {
        $("#rule_sets").empty();
        $("#device_sets").empty();
        $("#state_sets").empty();
        $("select#preset").empty();

        // Populate
        for(let rulSetOption of data["rule_set_options"].sort()) {
            $("#rule_sets").append('<div class="checkbox_option"><input type="checkbox" id="rule_set_' + rulSetOption + '" value="rule_set_' + rulSetOption + '"><label for="rule_set_' + rulSetOption + '">' + rulSetOption + '</label></div>');
        }

        for(let deviceSetOption of data["device_set_options"].sort()) {
            $("#device_sets").append('<div class="checkbox_option"><input type="checkbox" id="device_set_' + deviceSetOption + '" value="device_set_' + deviceSetOption + '"><label for="device_set_' + deviceSetOption + '">' + deviceSetOption + '</label></div>');
        }

        for(let stateSetOption of data["state_set_options"].sort()) {
            $("#state_sets").append('<div class="checkbox_option"><input type="checkbox" id="state_set_' + stateSetOption + '" value="state_set_' + stateSetOption + '"><label for="state_set_' + stateSetOption + '">' + stateSetOption + '</label></div>');
        }

        for(let presetOption of data["preset_options"].sort()) {
            $("select#preset").append('<option>' + presetOption + '</option>');
        }

        // Select
        selectRules(data["rule_set"]);
        selectDevices(data["device_set"]);
        selectStates(data["state_set"]);
        document.getElementById("preset").value = data["preset"];
    });
}

function selectRules(rules) {
    for(let rulSetSelection of rules) {
        $('#rule_set_' + rulSetSelection).prop('checked', true);
    }
}

function selectDevices(devices) {
    for(let deviceSetSelection of devices) {
        $('#device_set_' + deviceSetSelection).prop('checked', true);
    }
}

function selectStates(states) {
    for(let stateSetSelection of states) {
        $('#state_set_' + stateSetSelection).prop('checked', true);
    }
}


function setRuleProperties(HTMLID, enabled, available) {
    $("#" + HTMLID + "_enabled").prop('checked', enabled);
    $("#" + HTMLID + "_available").prop('checked', available);
}

function setDeviceProperties(HTMLID, enabled, available) {
    $("#" + HTMLID + "_enabled").prop('checked', enabled);
    $("#" + HTMLID + "_available").prop('checked', available);
}

function ruleIDToHTML(ruleName) {
    return ruleName.replace("rule.", "");
}

function deviceIDtoHTMLID(deviceID) {
    return deviceID.replace(".", "_"); // Only the first occurrence
}

function HTMLIDtoDeviveID(HTMLID) {
    return deviceID.replace("_", "."); // Only the first occurrence
}

function applyRuleProperties() {
    let ruleProperties = {};

    $.each($("#table_body_rules tr"), function () {
        let HTMLID = $(this).children("td").first().text();
        let enabled = $("#" + HTMLID + "_enabled").prop('checked');
        let available = $("#" + HTMLID + "_available").prop('checked');

        ruleProperties["rule." + HTMLID] = { enabled: enabled, available: available };
    });

    $.ajax({
        url:            "/intelligibleIoT/api/rules/",
        type:           "PUT",
        data: JSON.stringify(ruleProperties),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function applyConfig(listeningToConfig) {
    let config = {connected_to_hassio : listeningToConfig};

    $.ajax({
        url:            "/intelligibleIoT/api/config/",
        type:           "PUT",
        data: JSON.stringify(config),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function applyPredictions(predictions) {
    let config = {predictions : predictions};

    $.ajax({
        url:            "/intelligibleIoT/api/config/predictions/",
        type:           "PUT",
        data: JSON.stringify(config),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
        exportUseCase();
    });
}

function dateToString(date) {
    return date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear() + " " + timeToString(date);
}

function timeToString(date) {
    let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();

    return hours + ":" + minutes;
}

function clearUseCaseSets() {
    $.each($("#rule_sets input"), function () {
        $(this).prop('checked', false);
    });

    $.each($("#device_sets input"), function () {
        $(this).prop('checked', false);
    });

    $.each($("#state_sets input"), function () {
        $(this).prop('checked', false);
    });
}

function startExperiment() {
    let participant = {
        participant : $("#experiment_participant").val(),
        group: $("#experiment_group").val()
    };

    $.ajax({
        url:            "/intelligibleIoT/api/study/start/",
        type:           "PUT",
        data: JSON.stringify(participant),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
    }).done(function (data) {
        refresh();
    });
}

function nextStep() {
    $.ajax({
        url:            "/intelligibleIoT/api/study/next/",
        type:           "GET",
    }).done(function (data) {
        refresh();
    });
}