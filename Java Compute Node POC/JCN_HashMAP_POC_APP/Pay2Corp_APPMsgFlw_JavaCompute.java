
import java.util.Map.Entry;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.wallet247.clientutil.api.WalletAPI;
import com.wallet247.clientutil.bean.WalletParamMap;

public class Pay2Corp_APPMsgFlw_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		
		//Create new empty message
		
		MbMessage outMessage = new MbMessage();
		MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly, outMessage);

		String endPoingURL = "https://demo.b2biz.co.in/ws/walletAPI";
		String apiFor = "registerUser";
		String walletCode = "WT-1217";
		String walletPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFdzI00N6VwjKuM6VCthI384NLqDQcY4vLl69Ug1GA02pfybm0JvgWFP0UGcfZ0ODeVuxvYwluCoGYIGwI/whwXpSf54kjFlDLMBTdu1z2F5HmeLGq2iTcbEnvdAoAW7YV4qcKnebPJGtarxxwGsRDC4Pwua+Yvn0wnGnyVEGVswIDAQAB";
		String clientKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMaFCMqEQicVFeLrT76uWh2ZzRzJB/al8Gvfl9oHcrmJwFJknvqMROh6WaVasjVdS9swru77D3fp6iGFxyzc+76uYxqbsmjU4O3HjBaq57eZiG8HQ4i80ebpZbpjbjlq/ZrCoj4d4fWQtdNrin2WlWEuY0eDtoL5Rl2IO3vksTDDAgMBAAECgYAmXrI7SmLq4OVxDaBFL7fVPtPpO2Xho2MbxoS4LHZdLCXTBwW/UXtEH9nR3vzQ2a5+uCMnp7juqXSKdCvyHOg/NDo5fp1Wz35uAZJmGQ0dQKTMueb9SXm3BJlhnUPUNnSqUp5omCrvVK4ASakOcFKsS93BcLQ7bfpIfKCGob/Z0QJBAPdY4Hlm/nPdH+e0tiPD25bXgSv6SYRaDYIXB2mS2IHDLoNpb5JEwsnkd9o1FxEmw3vwNayvmzyu2O700RGIMLsCQQDNduGywk1mtY+rAROzWvIa3HlkdQSaKcnLinnZeLlS8vwoFj5QsPQiKQeAP8tCp/ByM2wWp5eZjbTsMohr/6OZAkEA5ROTEdypXHU2z0k2RvllrdX489nhrIoaaJkbbFNr4QH7WgmUI0s7e+/0cEsrCd90vJxDmaMpipIEp4pk9m/DcQJBAKR5G+crHyavBJjRPeH/VXsnLo26FrsJ5J3o9e2edEvwcuXsGGojnOqTiYuihaWQJixk+YuREQw8oa3KLea2N1ECQQCEm4MoDa6izPMTVppxvKco6dxBwiCwTtMGALbISIWSUoBegTDFuUsQD5+zOrj8bqEDSFpSrraQRCAjhAkitn4O";
		try {
			
						// optionally copy message headers
						copyMessageHeaders(inMessage, outMessage);
						// ----------------------------------------------------------
						// Add user code below
						 MbElement inJsonData = inMessage.getRootElement().getLastChild().getFirstChild().getFirstChild();
						 WalletParamMap map = new WalletParamMap();
						 while (inJsonData != null && inJsonData.getNextSibling() != null) {
							 map.put(inJsonData.getName(),inJsonData.getValueAsString());
								inJsonData = inJsonData.getNextSibling();
							}
						 WalletAPI walletAPI = new WalletAPI();
						 WalletParamMap responseMap = walletAPI.callWalletAPI(endPoingURL,apiFor,walletCode, map, walletPublicKey, clientKey);
						 MbElement outRoot = outMessage.getRootElement();
						 MbElement outJsonRoot = outRoot.createElementAsLastChild(MbJSON.PARSER_NAME);
						 MbElement outJsonData = outJsonRoot.createElementAsLastChild(MbElement.TYPE_NAME, MbJSON.DATA_ELEMENT_NAME, null);
						 for (Entry<String, String> entry : responseMap.entrySet()){
							  MbElement outJsonTest = outJsonData.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, entry.getKey(), entry.getValue());
						 }
						// End of user code
						// ----------------------------------------------------------
					} catch (MbException e) {
						// Re-throw to allow Broker handling of MbException
						throw e;
					} catch (RuntimeException e) {
						// Re-throw to allow Broker handling of RuntimeException
						throw e;
					} catch (Exception e) {
						// Consider replacing Exception with type(s) thrown by user code
						// Example handling ensures all exceptions are re-thrown to be handled in the flow
						throw new MbUserException(this, "evaluate()", "", "", e.toString(),
								null);
					}
					// The following should only be changed
					// if not propagating message to the 'out' terminal
					out.propagate(outAssembly);
				}

	private void copyMessageHeaders(MbMessage inMessage, MbMessage outMessage) {
		// TODO Auto-generated method stub
		
	}
}