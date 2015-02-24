package com.dome.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dome.viewer.R;

/**
 * ���ڴ����Ի���
 * 
 * @author MJ
 */
public class DialogBulder {
	Dialog dialog;
	Context context;

	public int dialogId;

	public DialogBulder(Context context, int dialogId) {
		Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.dilog_one);// ������Ҫ��ʾ������
		dialog.getWindow().getAttributes().width = 280;// ���ڶԻ���Ŀ��
		this.dialog = dialog;
		this.context = context;
		this.dialogId = dialogId;
	}

	public DialogBulder(Context context) {
		this(context, 0);
	}

	/**
	 * ���ñ���
	 * 
	 * @param title
	 */
	public DialogBulder setTitle(Object title) {
		// setTitle("��ʾ")
		// setTitle(R.string.tip)
		TextView titleView = getView(R.id.title);
		titleView.setText(parseParam(title));

		return this;
	}

	/**
	 * �����м������
	 */
	public DialogBulder setMessage(Object message) {
		TextView messageView = getView(R.id.message);
		messageView.setText(parseParam(message));

		return this;
	}

	Button left;

	public DialogBulder setButtons(Object leftBtn, Object rightBtn, final OnDialogButtonClickListener listener) {
		// ������߰�ť������
		Button left = getView(R.id.left);
		left.setText(parseParam(leftBtn));
		// ����ť�󶨼�����
		left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();

				if (listener != null) { // �м�����
					listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_LEFT);
				}
			}
		});
		this.left = left;

//		// �����ұ߰�ť������
//		Button right = getView(R.id.right);
//		right.setText(parseParam(rightBtn));
//		// ����ť�󶨼�����
//		right.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				dialog.dismiss();
//
//				if (listener != null) { // �м�����
//					listener.onDialogButtonClick(context, DialogBulder.this, dialog, dialogId, OnDialogButtonClickListener.BUTTON_RIGHT);
//				}
//			}
//		});
		return this;
	}

	/**
	 * ͨ��id�ҵ��Ի����ж�Ӧ���ӿؼ�
	 * 
	 * @param id
	 *            �ӿؼ���id
	 * @return �ӿؼ�
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int id) {
		return (T) dialog.findViewById(id);
	}

	/**
	 * ��������
	 * 
	 * @param param
	 *            �ַ��� ���� ��Դid
	 * @return ͳһ�����ַ���
	 */
	private String parseParam(Object param) {
		if (param instanceof Integer) {
			return context.getString((Integer) param);
		} else if (param instanceof String) {
			return param.toString();
		}
		return null;
	}

	/**
	 * ˵���Ѿ�ȷ���Ի���Ľ������
	 * 
	 * @return
	 */
	public Dialog create() {
		if (left == null) { // ˵������Ҫ��ť
			// �õ���ť���ڵĲ���
			ViewGroup btnsLayout = getView(R.id.btns_layout);
			// �õ����ڵ�
			ViewGroup root = getView(R.id.root);
			// �Ƴ���ť���ڵĲ���
			// ViewGroup root = (ViewGroup) btnsLayout.getParent();
			root.removeView(btnsLayout);
		}
		return dialog;
	}

	public interface OnDialogButtonClickListener {
		public static final int BUTTON_LEFT = 0;
		public static final int BUTTON_RIGHT = 1;

		public void onDialogButtonClick(Context context, DialogBulder builder, Dialog dialog, int dialogId, int which);
	}

	/**
	 * ��view�Ž��Ի�����
	 */
	public DialogBulder setView(View view) {
		// 1.���message���ڵĲ���
		ViewGroup messageLayout = getView(R.id.message_layout);
		// 2.�Ƴ�TextView
		messageLayout.removeAllViews();
		// 3.����µ�View
		messageLayout.addView(view);
		return this;
	}

	/**
	 * �������ļ���Ӧ��View�Ž��Ի�����
	 * 
	 * @param layout
	 * @return
	 */
	public DialogBulder setView(int layout) {
		ViewGroup parent = getView(R.id.message_layout);
		View view = LayoutInflater.from(context).inflate(layout, parent, false);
		return setView(view);
	}

	public DialogBulder setItems(String[] items, final OnDialogItemClickListener listener) {
		// 1.��ListView�Ž��Ի�����
		setView(R.layout.list_view);

		// 2.����ListView������
		ListView listView = getView(android.R.id.list);
		// item_text_view�ĸ��ڵ������TextView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_text_view, items);
		// new ArrayAdapter<T>(context, R.layout.item_text_view, R.id.text_view,
		// items)
		// listView.setAdapter(new SImple)
		listView.setAdapter(adapter);

		// 3.��ListView�󶨼�����
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dialog.dismiss();

				if (listener != null) {
					listener.onDialogItemClick(context, DialogBulder.this, dialog, position);
				}
			}
		});
		return this;
	}

	public DialogBulder setItems(int arrayId, OnDialogItemClickListener listener) {
		String[] items = context.getResources().getStringArray(arrayId);
		return setItems(items, listener);
	}

	public interface OnDialogItemClickListener {
		public void onDialogItemClick(Context context, DialogBulder builder, Dialog dialog, int position);
	}
}
