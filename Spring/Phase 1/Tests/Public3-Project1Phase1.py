from http import HTTPStatus
import requests

# Checks if the reInitialize in delivery-service is working
# make an agent as available by making that agent sign in
# reInitialize the delivery-service
# check the status of the agent. It must be signed-out


# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082

def test():
    test_result = 'Pass'

    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Check Agent 201 status
    http_response = requests.get("http://localhost:8081/agent/201")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "available":
        test_result = 'Fail'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    # Check Agent 201 status
    http_response = requests.get("http://localhost:8081/agent/201")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "signed-out":
        test_result = 'Fail'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
