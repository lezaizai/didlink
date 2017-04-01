package com.didlink.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckpointState {
	private static final Logger LOGGER = Logger.getLogger(CheckpointState.class
			.getName());

	private HashMap<String, String> m_hmCheckpoints = null;

	private String sStateStoreName;

	public CheckpointState(String sName) {
		this.sStateStoreName = sName;

		m_hmCheckpoints = new HashMap<String, String>();
	}

	public void addChkPtEntity(String sEntity, String sValue) {

		m_hmCheckpoints.put(sEntity, sValue);
	}

	public String getChkPtEntity(String sKey) {
		return m_hmCheckpoints.get(sKey);
	}

	public HashMap<String, String> getChkPtEntities() {
		return m_hmCheckpoints;
	}

	public void loadStateStore() throws Exception {
		String checkpoint = null;
		RandomAccessFile raf = null;

		try {

			raf = new RandomAccessFile(sStateStoreName, "rw");
			// set the file pointer at 0 position
			raf.seek(0);

			checkpoint = raf.readLine();
			if ((checkpoint != null) && (!checkpoint.equals(""))) {

				JSONObject jo = new JSONObject(checkpoint);

				m_hmCheckpoints = JSONUtils.toMap(jo);

			}

		} catch (IllegalArgumentException ex) {

			LOGGER.log(Level.INFO, "ERROR Invalid Checkpoint: " + checkpoint,
					ex);
			throw new Exception("Invalid Checkpoint: " + checkpoint, ex);
		} catch (JSONException ex) {

			LOGGER.log(Level.INFO, "ERROR Invalid Checkpoint: " + checkpoint,
					ex);
			throw new Exception("Invalid Checkpoint: " + checkpoint, ex);
		} catch (Exception ex) {
			LOGGER.log(Level.INFO, "ERROR Invalid Checkpoint: " + checkpoint,
					ex);
			throw new Exception("Invalid Checkpoint: " + checkpoint, ex);
		} finally {
			if (raf != null) {
				raf.close();
			}
		}

	}

	public void saveStateStore() throws Exception {

		RandomAccessFile raf = null;

		try {
			raf = new RandomAccessFile(sStateStoreName, "rw");

			// truncate the file
			raf.setLength(0);

			// set the file pointer at 0 position
			raf.seek(0);

			raf.writeBytes(asString());
		} catch (Exception ex) {
			LOGGER.log(Level.INFO, "ERROR Saving Checkpoint: " + asString(), ex);
		} finally {
			if (raf != null) {
				raf.close();
			}
		}
	}

	/**
	 * @returns the Checkpoint as a String.
	 * @throws Exception
	 *             if error.
	 */
	public String asString() throws Exception {
		String result = "";
		try {

			if (!m_hmCheckpoints.isEmpty()) {
				JSONObject jo = (JSONObject) JSONUtils.toJSON(m_hmCheckpoints);

				result = jo.toString();
				LOGGER.info("Created Checkpoint: " + result);
			}

			return result;
		} catch (JSONException ex) {

			LOGGER.log(Level.INFO, "ERROR JSON problem creating Checkpoint: ",
					ex);
			throw new Exception("JSON problem creating Checkpoint", ex);
		}
	}
}
