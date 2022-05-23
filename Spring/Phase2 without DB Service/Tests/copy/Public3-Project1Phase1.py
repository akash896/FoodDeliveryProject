from http import HTTPStatus
import requests


def test():
    test_result = 'Pass'



    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail1'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail2'

    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail3'

    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

    http_response = requests.get("http://localhost:8081/agent/201")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail5'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "available":
        test_result = 'Fail6'

    http_response = requests.post("http://localhost:8081/reInitialize")

    http_response = requests.get("http://localhost:8081/agent/201")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail7'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "signed-out":
        test_result = 'Fail8'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
